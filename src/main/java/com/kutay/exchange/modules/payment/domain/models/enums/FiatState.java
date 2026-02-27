package com.kutay.exchange.modules.payment.domain.models.enums;

public enum FiatState {
    CREATED, // user initiated the request
    PENDING_PROVIDER, // sent to stripe/paypal
    AUTHORIZED, // the bank said yes, but the money hasnt moved yet.
    SETTLED, // the money is in our bank account
    DECLINED // bank said no or used timed out
}
