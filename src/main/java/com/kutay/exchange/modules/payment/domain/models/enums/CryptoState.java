package com.kutay.exchange.modules.payment.domain.models.enums;

public enum CryptoState {
    DETECTED,
    CONFIRMING,
    FINALIZED,
    REORG_DROPPED,
    FAILED
}
