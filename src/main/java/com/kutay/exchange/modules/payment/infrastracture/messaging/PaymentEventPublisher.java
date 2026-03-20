package com.kutay.exchange.modules.payment.infrastracture.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kutay.exchange.modules.payment.domain.models.BankTransfer;
import com.kutay.exchange.modules.payment.infrastracture.messaging.events.FiatDepositRecorded;
import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.OutboxEvent;
import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.enums.AggregateType;
import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.enums.PaymentEventType;
import com.kutay.exchange.modules.payment.infrastracture.persistence.OutboxRepository;
import com.kutay.exchange.modules.payment.web.dto.FiatDepositWebhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void publish(FiatDepositWebhook fiatDepositWebhook, BankTransfer bankTransfer) {

        UUID eventId = UUID.randomUUID();

        FiatDepositRecorded fiatDepositRecorded = new FiatDepositRecorded(eventId.toString(),
                fiatDepositWebhook.nationalId(),
                fiatDepositWebhook.asset(),
                fiatDepositWebhook.amount().toString(),
                fiatDepositWebhook.bankRef());

        Map<String, Object> event = objectMapper.convertValue(fiatDepositRecorded,
                new TypeReference<Map<String, Object>>() {
                });

        OutboxEvent outboxEvent = new OutboxEvent(eventId,
                bankTransfer.getId().toString(),
                AggregateType.PAYMENT,
                PaymentEventType.BANK_DEPOSIT_RECORDED,
                event
        );

        outboxRepository.save(outboxEvent);
    }
}
