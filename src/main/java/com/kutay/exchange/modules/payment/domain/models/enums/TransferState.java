package com.kutay.exchange.modules.payment.domain.models.enums;

public enum TransferState {
    CREATED, // Deposit -> webhook received, payment record created - Withdraw -> user created request
    PROCESSING, // Deposit --> sent to ledger, wallet projection updating. - Withdraw -> request sent to bank's system
    COMPLETED, // Deposit --> funds credited to user's wallet - Withdraw -> bank confirmed funds sent
    FAILED, // bank rejected
    REVERSED // transfer returned by bank
}
