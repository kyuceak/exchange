package com.kutay.exchange.modules.payment.domain.service;

import jakarta.validation.constraints.NotNull;

public class DuplicatePaymentException extends RuntimeException {
    public DuplicatePaymentException(@NotNull String bankRef, Throwable e) {
        super("Duplicate Payment for bankRef: " + bankRef, e);
    }
}
