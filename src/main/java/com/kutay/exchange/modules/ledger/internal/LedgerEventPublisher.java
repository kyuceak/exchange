package com.kutay.exchange.modules.ledger.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kutay.exchange.modules.ledger.infrastructure.events.LedgerEntryRecorded;
import com.kutay.exchange.modules.ledger.infrastructure.outbox.AggregateType;
import com.kutay.exchange.modules.ledger.infrastructure.outbox.LedgerEventType;
import com.kutay.exchange.modules.ledger.infrastructure.outbox.OutboxEvent;
import com.kutay.exchange.modules.ledger.infrastructure.outbox.OutboxRepository;
import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountScope;
import com.kutay.exchange.modules.ledger.internal.entry.model.Entry;
import com.kutay.exchange.modules.ledger.internal.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LedgerEventPublisher {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void publishIfUserAccount(Transaction transaction, Entry entry) {
        if (entry.getAccount().getAccountScope() != AccountScope.USER) {
            return;
        }
        publish(transaction, entry);
    }

    private void publish(Transaction transaction, Entry entry) {
        // 1. get account
        Account account = entry.getAccount();

        // 2. create payload
        LedgerEntryRecorded event = LedgerEntryRecorded.from(
                entry.getId(),
                account.getWalletId(),
                account.getAsset(),
                entry.getAmount(),
                entry.getDirection(),
                entry.getLayer(),
                transaction.getReferenceId(),
                transaction.getPostedAt()
        );

        Map<String, Object> payload = objectMapper.convertValue(event, Map.class);

        OutboxEvent outboxEvent = new OutboxEvent(entry.getId().toString(),
                AggregateType.ENTRY.externalName(),
                LedgerEventType.LEDGER_ENTRY_CREATED,
                payload);

        outboxRepository.save(outboxEvent);
    }

}
