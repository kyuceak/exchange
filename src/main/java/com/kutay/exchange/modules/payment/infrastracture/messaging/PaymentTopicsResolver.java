package com.kutay.exchange.modules.payment.infrastracture.messaging;

import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.enums.PaymentEventType;
import com.kutay.exchange.shared.kafka.Topics;

public final class PaymentTopicsResolver {
    private PaymentTopicsResolver() {
    }

    public static String resolve(PaymentEventType eventType) {
        return switch (eventType) {
            case BANK_DEPOSIT_RECORDED, CARD_DEPOSIT_RECORDED -> Topics.FIAT_DEPOSIT_EVENTS;
            case CRYPTO_DEPOSIT_RECORDED -> Topics.CRYPTO_DEPOSIT_EVENTS;
            case BANK_WITHDRAWAL_RECORDED -> Topics.FIAT_WITHDRAWAL_EVENTS;
            case CRYPTO_WITHDRAWAL_RECORDED -> Topics.CRYPTO_WITHDRAWAL_EVENTS;
        };
    }
}
