package com.kutay.exchange.modules.payment.infrastracture.messaging.outbox;

import com.kutay.exchange.modules.payment.infrastracture.messaging.PaymentTopicsResolver;
import com.kutay.exchange.modules.payment.infrastracture.persistence.PaymentOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentOutboxRelayService {
    private final PaymentOutboxRepository paymentOutboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentOutboxResultBuffer paymentOutboxResultBuffer;

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void emitPendingEvents() {
        List<PaymentOutboxEvent> pendingEvents = paymentOutboxRepository.findRetryableEvents();

        if (pendingEvents.isEmpty()) return;

        Set<UUID> ids = pendingEvents.stream()
                .map(PaymentOutboxEvent::getId)
                .collect(Collectors.toSet());
        paymentOutboxRepository.markAsSending(ids, Instant.now());

        for (PaymentOutboxEvent event : pendingEvents) {
            kafkaTemplate.send(PaymentTopicsResolver.resolve(event.getPaymentEventType()),
                            event.getAggregateId(),
                            event.getPayload()).
                    whenComplete((result, ex) -> {
                        if (ex == null) {
                            paymentOutboxResultBuffer.addSuccess(event.getId());
                            log.info("Kafka ACK received for event: {}", event.getId());

                        } else {
                            paymentOutboxResultBuffer.addFailure(event.getId());
                            log.info("Kafka send failed for event {}: {}", event.getId(), ex.getMessage());
                        }
                    });
            log.info("Published event: type={}, aggregateId={}", event.getPaymentEventType(), event.getAggregateId());
        }

    }
}
