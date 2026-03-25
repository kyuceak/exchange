package com.kutay.exchange.modules.payment.infrastracture.messaging;

import com.kutay.exchange.modules.ledger.infrastructure.persistence.OutboxRepository;
import com.kutay.exchange.modules.payment.domain.models.BankTransfer;
import com.kutay.exchange.modules.payment.infrastracture.persistence.BankTransferRepository;
import com.kutay.exchange.modules.payment.infrastracture.persistence.PaymentOutboxRepository;
import com.kutay.exchange.shared.kafka.Topics;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {
    private final BankTransferRepository bankTransferRepository;

    @KafkaListener(
            topics = Topics.PAYMENT_STATUS_EVENTS,
            groupId = "payment-service" // define which consumer group a group belongs to.
            // with group Id
    )
    void handleStatusUpdateDepositEvents(ConsumerRecord<String, Map<String, Object>> consumerRecord) {
        log.info("Received kafka event: key={}, partition={}", consumerRecord.key(), consumerRecord.partition());

        try {
            String referenceId = (String) consumerRecord.value().get("referenceId");
            String status = (String) consumerRecord.value().get("status");
            BankTransfer bankTransfer = bankTransferRepository.findByReferenceId(referenceId)
                    .orElseThrow(() -> new EntityNotFoundException("such payment does not exist: referenceId=" + referenceId));

            if ("SUCCESS".equals(status)) {
                bankTransfer.markCompleted();
            } else {
                bankTransfer.markFailed();
            }
            bankTransferRepository.save(bankTransfer);
            log.info("BankTransfer status updated. id={}, status={}", bankTransfer.getId(), status);
        } catch (Exception e) {
            log.error("Failed to process event in PaymentEventConsumer: key={}, error={}", consumerRecord.key(), e.getMessage());
        }
    }

}
