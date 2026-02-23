package com.kutay.exchange.modules.payment.domain.models.enums;

public enum BlockchainNetwork {
    BITCOIN(6),
    ETHEREUM(12), // ETH, USDT (ERC-20)
    TRON(20); // USDT (TRC-20)

    private final int requiredConfirmations;

    BlockchainNetwork(int requiredConfirmations) {
        this.requiredConfirmations = requiredConfirmations;
    }

    public int getRequiredConfirmations() {
        return requiredConfirmations;
    }
}
