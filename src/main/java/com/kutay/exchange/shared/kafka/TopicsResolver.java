package com.kutay.exchange.shared.kafka;

import com.kutay.exchange.modules.ledger.infrastructure.outbox.LedgerEventType;
import org.springframework.stereotype.Component;

@Component
public class TopicsResolver {
    // This class tries to resolve the topic we passed
    public String resolve(LedgerEventType eventType) {
        return switch (eventType) {
            case LEDGER_ENTRY_CREATED, LEDGER_ENTRY_SETTLED -> Topics.LEDGER_EVENTS;
            case WALLET_BALANCE_UPDATED -> Topics.WALLET_EVENTS;
        };
    }
}
