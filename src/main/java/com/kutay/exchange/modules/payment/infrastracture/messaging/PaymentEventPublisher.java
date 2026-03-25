package com.kutay.exchange.modules.payment.infrastracture.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kutay.exchange.modules.payment.domain.models.BankTransfer;
import com.kutay.exchange.modules.payment.infrastracture.messaging.events.FiatDepositRecorded;
import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.PaymentOutboxEvent;
import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.enums.AggregateType;
import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.enums.PaymentEventType;
import com.kutay.exchange.modules.payment.infrastracture.persistence.PaymentOutboxRepository;
import com.kutay.exchange.modules.payment.web.dto.FiatDepositWebhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final PaymentOutboxRepository paymentOutboxRepository;
    private final ObjectMapper objectMapper;

    public void publish(FiatDepositWebhook fiatDepositWebhook, BankTransfer bankTransfer) {


        FiatDepositRecorded fiatDepositRecorded = new FiatDepositRecorded(
                bankTransfer.getWalletId().toString(),
                fiatDepositWebhook.nationalId(),
                fiatDepositWebhook.asset(),
                fiatDepositWebhook.amount().toString(),
                bankTransfer.getReferenceId());

        Map<String, Object> event = objectMapper.convertValue(fiatDepositRecorded,
                new TypeReference<Map<String, Object>>() {
                });

        PaymentOutboxEvent paymentOutboxEvent = new PaymentOutboxEvent(
                bankTransfer.getId().toString(),
                AggregateType.PAYMENT,
                PaymentEventType.BANK_DEPOSIT_RECORDED,
                event
        );

        paymentOutboxRepository.save(paymentOutboxEvent);
        bankTransfer.markProcessing();
    }
}
