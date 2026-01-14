package com.kutay.exchange.modules.ledger.internal.entry;

import com.kutay.exchange.modules.ledger.infrastructure.persistence.LedgerEntryRepository;
import com.kutay.exchange.modules.ledger.internal.LedgerEventPublisher;
import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.entry.model.Entry;
import com.kutay.exchange.modules.ledger.internal.entry.model.enums.EntryLayer;
import com.kutay.exchange.modules.ledger.internal.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntryFactory {
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerEventPublisher ledgerEventPublisher;

    // create and validate entries
    @Transactional
    public List<Entry> createDoubleEntry(Transaction transaction, Account debitAccount,
                                         Account creditAccount, BigDecimal amount, EntryLayer layer) {
        Entry debitEntry = createDebitEntry(transaction, debitAccount, amount, layer);
        Entry creditEntry = createCreditEntry(transaction, creditAccount, amount, layer);
        return List.of(creditEntry, debitEntry);
    }

    // HELPER METHODS
    public Entry createDebitEntry(Transaction transaction, Account account, BigDecimal amount, EntryLayer layer) {

        Entry entry = Entry.debit(account, transaction, amount, layer);
        Entry saved = ledgerEntryRepository.save(entry);
        ledgerEventPublisher.publishIfUserAccount(transaction, saved);
        return saved;
    }


    public Entry createCreditEntry(Transaction transaction, Account account, BigDecimal amount, EntryLayer layer) {
        Entry entry = Entry.credit(account, transaction, amount, layer);
        Entry saved = ledgerEntryRepository.save(entry);
        ledgerEventPublisher.publishIfUserAccount(transaction, saved);
        return saved;
    }
}
