package com.kutay.exchange.shared.kafka;

// final because topics are immutable, shared contract. must never change at runtime via inheritance.
public final class Topics {
    public static final String LEDGER_EVENTS = "ledger_events"; // our kafka topic name
    public static final String WALLET_EVENTS = "wallet_events";

    private Topics() {
    } // prevent instantiation

    // Topics class is a namespace and constants holder, a contract definition.
    // so we make constructor private so that there will be no valid runtime instance of this class.

}
