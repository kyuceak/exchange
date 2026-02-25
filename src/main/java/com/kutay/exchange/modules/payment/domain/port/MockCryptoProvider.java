package com.kutay.exchange.modules.payment.domain.port;

import com.kutay.exchange.modules.payment.domain.models.Payment;
import com.kutay.exchange.modules.payment.domain.port.dto.PaymentProviderResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockCryptoProvider implements PaymentProvider {
    @Override
    public PaymentProviderResult process(Payment payment) {
        // simulation: %90 success, random delay, returning fake txHash
        boolean success = Math.random() > 0.1;
        return success
                ? new PaymentProviderResult(true,
                UUID.randomUUID().toString(), "")
                : new PaymentProviderResult(false,
                UUID.randomUUID().toString(), "Simulated blockchain timeout");
    }
}
