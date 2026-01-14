package com.kutay.exchange.modules.ledger.internal;

import com.kutay.exchange.modules.ledger.api.LedgerFacade;
import com.kutay.exchange.modules.ledger.web.dto.RecordTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of {@link LedgerFacade}.
 * Delegates to {@link LedgerService} for business logic.
 */
@Service
@RequiredArgsConstructor
public class LedgerFacadeImpl implements LedgerFacade {
    private final LedgerService ledgerService;

    @Override
    public UUID recordGenericTransaction(RecordTransactionRequest request) {
        return ledgerService.recordGenericTransaction(request);
    }
}
