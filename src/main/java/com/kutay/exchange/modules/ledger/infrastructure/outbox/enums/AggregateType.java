package com.kutay.exchange.modules.ledger.infrastructure.outbox.enums;

/**
 * Types of aggregates that can produce domain events.
 * Used for event routing and categorization in the outbox pattern.
 */
public enum AggregateType {
    ENTRY("Entry"),
    TRANSACTION("Transaction"),
    ACCOUNT("Account");

    private final String externalName;

    AggregateType(String externalName) {
        this.externalName = externalName;
    }

    public String externalName() {
        return externalName;
    }
}