package com.kutay.exchange.modules.payment.domain.port;

import com.kutay.exchange.modules.payment.domain.models.Payment;
import com.kutay.exchange.modules.payment.domain.port.dto.PaymentProviderResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockFiatProvider implements PaymentProvider {
    @Override
    public PaymentProviderResult process(Payment payment) {
        boolean success = Math.random() > 0.1;
        return success ?
                new PaymentProviderResult(success,
                        UUID.randomUUID().toString(), "")
                : new PaymentProviderResult(success, null, "Simulated bank rejection");
    }
}
