package com.kutay.exchange.modules.ledger.internal;

import com.kutay.exchange.modules.ledger.api.LedgerFacade;
import com.kutay.exchange.modules.ledger.api.dto.LedgerAccountSpec;
import com.kutay.exchange.modules.ledger.api.dto.LedgerIntent;
import com.kutay.exchange.modules.ledger.domain.LedgerIntentResolver;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.AccountRepository;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.LedgerEntryRepository;
import com.kutay.exchange.modules.ledger.internal.account.LedgerAccountFactory;
import com.kutay.exchange.modules.ledger.api.dto.InternalTransaction;
import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountState;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link LedgerFacade}.
 * Delegates to {@link LedgerService} for business logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerFacadeImpl implements LedgerFacade {
    private final LedgerService ledgerService;
    private final LedgerAccountFactory ledgerAccountFactory;
    private final LedgerIntentResolver intentResolver;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountRepository accountRepository;

    @Override
    public UUID recordGenericTransactionIntent(LedgerIntent intent) {
        List<InternalTransaction.EntryLine> entries = intentResolver.resolve(intent);

        InternalTransaction transaction = new InternalTransaction(intent.referenceId(),
                intent.transactionType(),
                null,
                entries);

        return ledgerService.recordGenericTransaction(transaction);
    }

    @Override
    public void createUserAccount(LedgerAccountSpec spec) {
        ledgerAccountFactory.createUserLedger(spec);
    }

    @Override
    @Transactional
    public void reserve(LedgerIntent intent) {
        BigDecimal available = ledgerEntryRepository
                .calculateBalance(intent.walletId(),
                        intent.asset());

        if (available.compareTo(intent.amount()) < 0) {
            throw new IllegalStateException
                    ("Insufficient available balance: " + available +
                            " you need: " + intent.amount());
        }
        recordGenericTransactionIntent(intent);
    }

    @Override
    @Transactional
    public void release(LedgerIntent intent) {
        Account pendingAccount = accountRepository
                .findByWalletIdAndAssetAndState(intent.walletId(), intent.asset(), AccountState.PENDING_DEBIT)
                .orElseThrow(() ->
                        new EntityNotFoundException("account not found with walletId: " + intent.walletId()
                                + " asset: " + intent.asset()));

        if (pendingAccount.getBalance().compareTo(intent.amount()) < 0) {
            throw new IllegalStateException("No reserved funds to release");
        }

        recordGenericTransactionIntent(intent);
    }

    @Override
    public void settle(LedgerIntent intent) {
        recordGenericTransactionIntent(intent);
    }
}
