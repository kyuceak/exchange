package com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.enums;

public enum PaymentEventType {
    BANK_DEPOSIT_RECORDED,
    BANK_WITHDRAWAL_RECORDED,
    CRYPTO_DEPOSIT_RECORDED,
    CRYPTO_WITHDRAWAL_RECORDED,
    CARD_DEPOSIT_RECORDED,
}
