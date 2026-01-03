package com.kutay.exchange.modules.ledger.internal;

import com.kutay.exchange.modules.ledger.api.dto.RecordEntryRequest;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.AccountRepository;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.LedgerEntryRepository;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.TransactionRepository;
import com.kutay.exchange.modules.ledger.internal.model.Account;
import com.kutay.exchange.modules.ledger.internal.model.Entry;
import com.kutay.exchange.modules.ledger.internal.model.Transaction;
import com.kutay.exchange.modules.ledger.internal.model.enums.EntryLayer;
import com.kutay.exchange.modules.ledger.internal.model.enums.TransactionStatus;
import com.kutay.exchange.modules.ledger.internal.outbox.EventType;
import com.kutay.exchange.modules.ledger.internal.outbox.OutboxEvent;
import com.kutay.exchange.modules.ledger.internal.outbox.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountRepository accountRepository;
    private final OutboxRepository outboxRepository;


    /* Functionalities
     * CRUD accounts,
     * record transactions, entries
     *
     *
     *
     *
     * */


    public void recordEntry(RecordEntryRequest request) {
        // 1. check idempotency

        if (transactionRepository.existsByReferenceId(request.referenceId())) {
            return;
        }

        // 2. find or create account

        Account account = accountRepository
                .findByWalletIdAndAsset(request.walletId(), request.asset())
                .orElseThrow(() -> new IllegalStateException("Account not found"));


        // 3. create transaction

        Transaction transaction = Transaction.builder()
                .referenceId(request.referenceId())
                .description(request.description())
                .status(TransactionStatus.POSTED)
                .postedAt(Instant.now())
                .build();
        transactionRepository.save(transaction);

        // 4. create entry

        Entry entry = Entry.builder()
                .transaction(transaction)
                .account(account)
                .direction(request.entryDirection())
                .amount(request.amount())
                .layer(request.entryLayer())
                .settled(false)
                .build();
        ledgerEntryRepository.save(entry);

        // 5. emit event

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateId(entry.getId().toString())
                .aggregateType("Entry")
                .eventType(EventType.LEDGER_ENTRY_CREATED)
                .payload(Map.of(
                        "entryId", entry.getId().toString(),
                        "walletId", request.walletId().toString(),
                        "asset", request.asset().name(),
                        "amount", request.amount().toString(),
                        "direction", request.entryDirection().name(),
                        "layer", request.entryLayer().name()

                ))
                .build();
        outboxRepository.save(outboxEvent);
    }


}
