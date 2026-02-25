package com.kutay.exchange.modules.payment.domain.port;

import com.kutay.exchange.modules.payment.domain.models.Payment;
import com.kutay.exchange.modules.payment.domain.port.dto.PaymentProviderResult;

public interface PaymentProvider {
    PaymentProviderResult process(Payment payment);
}
