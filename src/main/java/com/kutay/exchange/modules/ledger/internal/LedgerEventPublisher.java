package com.kutay.exchange.modules.ledger.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kutay.exchange.modules.ledger.infrastructure.events.LedgerTransactionRecorded;
import com.kutay.exchange.modules.ledger.infrastructure.outbox.enums.AggregateType;
import com.kutay.exchange.modules.ledger.infrastructure.outbox.enums.LedgerEventType;
import com.kutay.exchange.modules.ledger.infrastructure.outbox.OutboxEvent;
import com.kutay.exchange.modules.ledger.infrastructure.outbox.OutboxRepository;
import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountScope;
import com.kutay.exchange.modules.ledger.internal.entry.model.Entry;
import com.kutay.exchange.modules.ledger.internal.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LedgerEventPublisher {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void publishIfUserAccount(Transaction transaction, List<Entry> entries) {
        List<Entry> userEntries = new ArrayList<>();
        for (Entry entry : entries) {
            if (entry.getAccount().getAccountScope() == AccountScope.USER)
                userEntries.add(entry);
        }

        if (userEntries.isEmpty()) {
            return;
        }

        publish(transaction, userEntries);
    }

    private void publish(Transaction transaction, List<Entry> entries) {
        // 1. get account
        Account account = entries.get(0).getAccount();

        // 2. create payload
        List<LedgerTransactionRecorded.TransactionEntry> transactionEntries = entries.stream()
                .map(entry -> new LedgerTransactionRecorded.TransactionEntry(
                        entry.getId().toString(),
                        entry.getAccount().getState().name(),
                        entry.getDirection().name(),
                        entry.getAmount().toString()))
                .toList();

        LedgerTransactionRecorded event = LedgerTransactionRecorded.from(
                transaction.getId(),
                account.getWalletId(),
                account.getAsset(),
                transaction.getReferenceId(),
                transaction.getTransactionType(),
                transactionEntries
        );


        Map<String, Object> payload = objectMapper.convertValue(event, Map.class);

        OutboxEvent outboxEvent = new OutboxEvent(transaction.getId().toString(),
                AggregateType.TRANSACTION.externalName(),
                LedgerEventType.LEDGER_ENTRY_CREATED,
                payload);

        outboxRepository.save(outboxEvent);
    }

}
