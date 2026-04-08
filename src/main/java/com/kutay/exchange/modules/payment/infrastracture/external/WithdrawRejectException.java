package com.kutay.exchange.modules.payment.infrastracture.external;

public class WithdrawRejectException extends Exception {
    public WithdrawRejectException(String message) {
        super(message);
    }
}
