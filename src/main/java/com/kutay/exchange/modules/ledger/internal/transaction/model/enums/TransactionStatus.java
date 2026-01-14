package com.kutay.exchange.modules.ledger.internal.transaction.model.enums;

/**
 * Status of a ledger transaction.
 * <p>
 * PENDING: Transaction created but not yet finalized
 * POSTED: Transaction finalized and entries recorded
 * VOIDED: Transaction cancelled/reversed
 */
public enum TransactionStatus {
    PENDING,
    POSTED,
    VOIDED
}
