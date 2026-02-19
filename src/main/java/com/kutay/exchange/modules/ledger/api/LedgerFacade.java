package com.kutay.exchange.modules.ledger.api;

import com.kutay.exchange.modules.ledger.api.dto.LedgerAccountSpec;
import com.kutay.exchange.modules.ledger.web.dto.RecordTransactionRequest;

import java.util.UUID;

/**
 * Public API for the Ledger module.
 * Other modules (Wallet, Trading, etc.) use this facade to record and query ledger entries.
 * <p>
 * The ledger is the source of truth for all balance changes.
 * Every deposit, withdrawal, trade, and fee is recorded here.
 */
public interface LedgerFacade {

    UUID recordGenericTransaction(RecordTransactionRequest request);

    UUID createUserAccount(LedgerAccountSpec spec);
}
