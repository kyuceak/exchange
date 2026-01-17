package com.kutay.exchange.modules.ledger.infrastructure.outbox;

import com.kutay.exchange.shared.kafka.TopicsResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
// reads events from the outbox table and publishes them to the message broker
public class OutboxRelayService {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TopicsResolver topicsResolver;
    private final OutboxResultBuffer outboxResultBuffer;

    @Scheduled(fixedRate = 5000) // every 500ms --> Run this method automatically on a schedule
    @Transactional
    public void publishPendingEvents() {
        // 1. Fetch retryable events (PENDING or FAILED)
        List<OutboxEvent> events = outboxRepository.findRetryableEvents();

        if (events.isEmpty()) return;

        // 2. mark all as SENDING (BatchUpdate)
        Set<UUID> ids = events.stream()
                .map(OutboxEvent::getId)
                .collect(Collectors.toSet());
        outboxRepository.marksAsSending(ids, Instant.now());


        // publish unprocessed events
        for (OutboxEvent event : events) {
            // KafkaTemplate throws runtime exceptions, we still need to wrap it with try catch
            try {
                kafkaTemplate.send(topicsResolver.resolve(event.getEventType()), // non-blocking operation, it returns SendResult<K,V>(contains kafka metada about the sent reocrd) wrapped by CompletableFuture
                                event.getAggregateId(),
                                event.getPayload())
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                outboxResultBuffer.addSuccess(event.getId());
                                log.debug("Kafka ACK received for event: {}", event.getId());
                            } else {
                                outboxResultBuffer.addFailure(event.getId());
                                log.error("Kafka send failed for event {}: {}", event.getId(), ex.getMessage());
                            }
                        });
                log.info("Published event: type={}, aggregateId={}", event.getEventType(), event.getAggregateId());
            } catch (Exception e) {
                outboxResultBuffer.addFailure(event.getId());
                log.error("Failed to publish event: id={}, error={}", event.getId(), e.getMessage());
                // Don't mark as processed - will retry on next poll
            }
        }
        log.info("Dispatched {} events to Kafka", events.size());
    }

}
