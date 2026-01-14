package com.kutay.exchange.modules.ledger.infrastructure.events;

import com.kutay.exchange.modules.ledger.internal.entry.model.enums.EntryDirection;
import com.kutay.exchange.modules.ledger.internal.entry.model.enums.EntryLayer;
import com.kutay.exchange.shared.model.Asset;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

//UUID entryId,
//UUID walletId, // to find walletAsset
//Asset asset, // to find WalletAsset
//BigDecimal amount, // the amount of money
//EntryDirection direction, // DEBIT or CREDIT
//EntryLayer layer,
//String referenceId, // for idempotency in wallet
//Instant occurredAt //

public record LedgerEntryRecorded(String entryId,
                                  String walletId,
                                  String asset,
                                  String amount,
                                  String direction,
                                  String layer,
                                  String referenceId,
                                  Instant occurredAt) {
    // implement factory method
    public static LedgerEntryRecorded from(UUID entryId,
                                           UUID walletID,
                                           Asset asset,
                                           BigDecimal amount,
                                           EntryDirection direction,
                                           EntryLayer layer,
                                           String referenceId,
                                           Instant occurredAt
    ) {
        return new LedgerEntryRecorded(
                entryId.toString(),
                walletID.toString(),
                asset.name(),
                amount.toString(),
                direction.name(),
                layer.name(),
                referenceId,
                occurredAt);
    }
}
