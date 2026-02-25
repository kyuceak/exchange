package com.kutay.exchange.modules.payment.domain.port.dto;

public record PaymentProviderResult(boolean success, String providerRef, String failureReason) {
}
