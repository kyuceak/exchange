package com.kutay.exchange.modules.ledger.infrastructure.outbox;

public enum LedgerEventType {
    LEDGER_ENTRY_CREATED("LedgerEntryCreated"),
    LEDGER_ENTRY_SETTLED("LedgerEntryCreated"),
    WALLET_BALANCE_UPDATED("WalletBalanceUpdated");

    private final String externalName;

    LedgerEventType(String externalName) {
        this.externalName = externalName;
    }

    public String externalName() {
        return externalName;
    }
}
