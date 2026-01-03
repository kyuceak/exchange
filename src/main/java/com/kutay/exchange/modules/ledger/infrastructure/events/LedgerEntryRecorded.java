package com.kutay.exchange.modules.ledger.infrastructure.events;

import com.kutay.exchange.modules.ledger.internal.model.enums.EntryDirection;
import com.kutay.exchange.modules.ledger.internal.model.enums.EntryLayer;
import com.kutay.exchange.shared.model.Asset;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LedgerEntryRecorded(
        UUID entryId,
        UUID walletId, // to find walletAsset
        Asset asset, // to find WalletAsset
        BigDecimal amount, // the amount of money
        EntryDirection direction, // DEBIT or CREDIT
        EntryLayer layer,
        String referenceId, // for idempotency in wallet
        Instant occurredAt //
) {
}
