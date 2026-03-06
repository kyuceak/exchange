package com.kutay.exchange.modules.ledger.internal.account.model.enums;

public enum SystemAccountPurpose {
    CRYPTO_HOLDINGS,
    TRADING_FEES,
    WITHDRAWAL_FEES,
    ADJUSTMENTS, // cost center --> adjustments for user balance corrections in user's favor
    PENDING_PAYMENTS
}