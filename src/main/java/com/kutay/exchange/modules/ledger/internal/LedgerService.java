package com.kutay.exchange.modules.ledger.internal;

import com.kutay.exchange.modules.ledger.exception.LedgerImbalanceException;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.AccountRepository;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.LedgerEntryRepository;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.TransactionRepository;
import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.entry.model.Entry;
import com.kutay.exchange.shared.enums.EntryDirection;
import com.kutay.exchange.modules.ledger.internal.transaction.TransactionFactory;
import com.kutay.exchange.modules.ledger.internal.transaction.model.Transaction;
import com.kutay.exchange.modules.ledger.web.dto.RecordTransactionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerService {
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountRepository accountRepository;
    private final LedgerEventPublisher ledgerEventPublisher;

    /*
     * High level functions
     * 1. create Transaction
     * 2. record deposit
     * 3 record withdraw
     * */

    // supplying all the Account IDs
    @Transactional
    public UUID recordGenericTransaction(RecordTransactionRequest request) {

        // create transaction
        // add ledger Entries (DEBIT,CREDIT) to transaction

        // Idempotency Check or create transaction
        Transaction transaction = transactionFactory.createOrFindIfExists(request.referenceId(),
                request.description(),
                request.transactionType());

        // fetch batch Accounts and make sure them exist and then cache

        Set<UUID> accountIds = request.entries()
                .stream()
                .map(RecordTransactionRequest.EntryLine::accountId)
                .collect(Collectors.toSet());

        Map<UUID, Account> accounts = accountRepository.findAllById(accountIds)
                .stream()
                .collect(Collectors.toMap(Account::getId, account -> account));

        if (accounts.size() != accountIds.size()) {
            log.error("AccountId={}, accounts={}", accountIds, accounts);
            throw new IllegalStateException("One or more accounts not found");
        }

        // build the entries in memory
        List<Entry> entries = new ArrayList<>();
        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;
        for (RecordTransactionRequest.EntryLine line : request.entries()) {
            Account account = accounts.get(line.accountId());

            if (line.direction() == EntryDirection.DEBIT) {
                debitAmount = debitAmount.add(line.amount());
                entries.add(Entry.debit(account, transaction, line.amount(), line.layer()));
            } else {
                creditAmount = creditAmount.add(line.amount());
                entries.add(Entry.credit(account, transaction, line.amount(), line.layer()));
            }
        }

        if (debitAmount.compareTo(creditAmount) != 0) {
            log.error("Debit and credit amounts are not equal, debit={}, credit={}", debitAmount, creditAmount);
            throw new LedgerImbalanceException(debitAmount, creditAmount);
        }

        // persist the entries

        List<Entry> saved = ledgerEntryRepository.saveAll(entries);

        for (Entry entry : saved) {
            ledgerEventPublisher.publishIfUserAccount(transaction, entry);
        }
        return transaction.getId();
    }


}
