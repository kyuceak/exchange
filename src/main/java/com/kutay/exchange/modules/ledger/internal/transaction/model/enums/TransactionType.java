package com.kutay.exchange.modules.ledger.internal.transaction.model.enums;

/**
 * Types of ledger entries representing different transaction categories.
 * Used by other modules when recording ledger entries.
 */
public enum TransactionType {

    // Funding operations
    DEPOSIT,
    WITHDRAWAL,

    // Trading operations
    TRADE_BUY,
    TRADE_SELL,
    TRADE_FEE,

    // Internal transfers
    TRANSFER_IN,
    TRANSFER_OUT,

    // Adjustments
    REFUND,
    CORRECTION
}
