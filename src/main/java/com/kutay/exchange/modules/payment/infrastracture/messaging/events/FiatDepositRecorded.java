package com.kutay.exchange.modules.payment.infrastracture.messaging.events;

public record FiatDepositRecorded(
//        String eventId, // domain object Id  --> identifies the kafka message --> used for idempotency in ledger.
        String walletId,
        String nationalId,
        String asset,
        String amount,
        String referenceId // bankref --> identifies the business operation
) {
}
