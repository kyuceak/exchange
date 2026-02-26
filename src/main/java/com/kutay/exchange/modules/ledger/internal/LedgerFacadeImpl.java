package com.kutay.exchange.modules.ledger.internal;

import com.kutay.exchange.modules.ledger.api.LedgerFacade;
import com.kutay.exchange.modules.ledger.api.dto.LedgerAccountSpec;
import com.kutay.exchange.modules.ledger.api.dto.LedgerIntent;
import com.kutay.exchange.modules.ledger.domain.LedgerIntentResolver;
import com.kutay.exchange.modules.ledger.internal.account.LedgerAccountFactory;
import com.kutay.exchange.modules.ledger.api.dto.InternalTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link LedgerFacade}.
 * Delegates to {@link LedgerService} for business logic.
 */
@Service
@RequiredArgsConstructor
public class LedgerFacadeImpl implements LedgerFacade {
    private final LedgerService ledgerService;
    private final LedgerAccountFactory ledgerAccountFactory;
    private final LedgerIntentResolver intentResolver;

    @Override
    public UUID recordGenericTransaction(InternalTransaction request) {
        return ledgerService.recordGenericTransaction(request);
    }

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
}
