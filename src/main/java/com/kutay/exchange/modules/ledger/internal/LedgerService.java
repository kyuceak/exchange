package com.kutay.exchange.modules.ledger.internal;

import com.kutay.exchange.modules.ledger.api.dto.LedgerIntent;
import com.kutay.exchange.modules.ledger.exception.LedgerImbalanceException;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.AccountRepository;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.LedgerEntryRepository;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.TransactionRepository;
import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.entry.model.Entry;
import com.kutay.exchange.shared.contracts.EntryDirection;
import com.kutay.exchange.modules.ledger.internal.transaction.model.Transaction;
import com.kutay.exchange.modules.ledger.api.dto.InternalTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerService {
    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountRepository accountRepository;
    private final LedgerEventPublisher ledgerEventPublisher;

    /*
     * High level functions
     * 1. create Transaction

     * */

    // supplying all the Account IDs
    @Transactional
    public UUID recordGenericTransaction(InternalTransaction request) {

        // Idempotency check — reject duplicate referenceId
        if (transactionRepository.existsByReferenceId(request.referenceId())) {
            throw new IllegalStateException("Transaction with referenceId " + request.referenceId() + " already exists");
        }

        Transaction transaction = transactionRepository.save(
                new Transaction(request.referenceId(), request.description(), request.transactionType())
        );

        // fetch batch Accounts and make sure them exist and then cache

        Set<UUID> accountIds = request.entries()
                .stream()
                .map(InternalTransaction.EntryLine::accountId)
                .collect(Collectors.toSet());

        Map<UUID, Account> accounts = accountRepository.findAllById(accountIds)
                .stream()
                .collect(Collectors.toMap(Account::getId, account -> account));

        if (accounts.size() != accountIds.size()) {
            log.error("AccountId={}, accounts={}", accountIds, accounts);
            throw new IllegalStateException("One or more accounts not found");
        }

        // create transaction
        // add ledger Entries (DEBIT,CREDIT) to transaction
        // build the entries in memory
        List<Entry> entries = new ArrayList<>();

        for (InternalTransaction.EntryLine line : request.entries()) {
            Account account = accounts.get(line.accountId());
            if (line.direction() == EntryDirection.DEBIT) {
                entries.add(Entry.debit(account, transaction, line.amount(), line.layer()));
            } else {
                entries.add(Entry.credit(account, transaction, line.amount(), line.layer()));
            }
        }

        // persist the entries

        List<Entry> saved = ledgerEntryRepository.saveAll(entries);

        for (Entry entry : saved) {
            Account account = entry.getAccount();
            account.addToBalance(entry.getSignedAmount());
            ledgerEventPublisher.publishIfUserAccount(transaction, entry);
        }
        return transaction.getId();
    }
}
