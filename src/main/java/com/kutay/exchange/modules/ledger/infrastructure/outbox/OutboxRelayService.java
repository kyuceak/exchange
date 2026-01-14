package com.kutay.exchange.modules.ledger.infrastructure.outbox;

import com.kutay.exchange.shared.kafka.TopicsResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
// reads events from the outbox table and publishes them to the message broker
public class OutboxRelayService {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TopicsResolver topicsResolver;

    @Scheduled(fixedRate = 5000) // every 500ms --> Run this method automatically on a schedule
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> pendingEvents = outboxRepository.findByProcessedAtIsNullOrderByCreatedAtAsc();

        // publish unprocessed events
        for (OutboxEvent event : pendingEvents) {
            // KafkaTemplate throws runtime exceptions, we still need to wrap it with try catch
            try {
                kafkaTemplate.send(topicsResolver.resolve(event.getEventType()),
                        event.getAggregateId(),
                        event.getPayload()); // non-blocking operation, it returns SendResult<K,V>(contains kafka metada about the sent reocrd) wrapped by CompletableFuture
                event.markProcessed();
                outboxRepository.save(event); // .save means persist the entity if it's new, or merge it if it already exists.
                log.info("Published event: type={}, aggregateId={}", event.getEventType(), event.getAggregateId());
            } catch (Exception e) {
                log.error("Failed to publish event: id={}, error={}", event.getId(), e.getMessage());
                // Don't mark as processed - will retry on next poll
            }

        }
    }

}
