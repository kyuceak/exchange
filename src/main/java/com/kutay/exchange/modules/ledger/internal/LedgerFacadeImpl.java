package com.kutay.exchange.modules.ledger.internal;

import com.kutay.exchange.modules.ledger.api.LedgerFacade;
import com.kutay.exchange.modules.ledger.api.dto.LedgerAccountSpec;
import com.kutay.exchange.modules.ledger.api.dto.LedgerIntent;
import com.kutay.exchange.modules.ledger.domain.LedgerIntentResolver;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.AccountRepository;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.LedgerEntryRepository;
import com.kutay.exchange.modules.ledger.internal.account.AccountCodeGenerator;
import com.kutay.exchange.modules.ledger.internal.account.LedgerAccountFactory;
import com.kutay.exchange.modules.ledger.api.dto.InternalTransaction;
import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.contracts.EntryLayer;
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
    public UUID createUserAccount(LedgerAccountSpec spec) {
        return ledgerAccountFactory.createUserAccount(spec);
    }

    @Override
    @Transactional
    public void reserve(LedgerIntent intent) {
        BigDecimal available = ledgerEntryRepository
                .calculateBalance(intent.walletId(),
                        intent.asset(), EntryLayer.AVAILABLE);

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
        String code = AccountCodeGenerator.generateSystem(AccountType.LIABILITY, intent.asset(), SystemAccountPurpose.PENDING_PAYMENTS);
        Account pendingAccount = accountRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("such account does not exist"));

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
