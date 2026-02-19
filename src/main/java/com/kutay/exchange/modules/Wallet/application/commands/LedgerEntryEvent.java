package com.kutay.exchange.modules.wallet.application.commands;

import com.kutay.exchange.shared.enums.EntryDirection;
import com.kutay.exchange.shared.enums.EntryLayer;
import com.kutay.exchange.shared.model.Asset;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record LedgerEntryEvent(
        UUID entryId,
        UUID walletId,
        Asset asset,
        BigDecimal amount,
        EntryDirection direction,
        EntryLayer layer,
        String referenceId,
        Instant occurredAt) {
    // factory pattern
    public static LedgerEntryEvent fromPayload(Map<String, Object> payload) {
        return new LedgerEntryEvent(
                UUID.fromString((String) payload.get("entryId")),
                UUID.fromString((String) payload.get("walletId")),
                Asset.valueOf((String) payload.get("asset")),
                new BigDecimal((String) payload.get("amount")),
                EntryDirection.valueOf((String) payload.get("direction")),
                EntryLayer.valueOf((String) payload.get("layer")),
                (String) payload.get("referenceId"),
                Instant.parse(payload.get("occurredAt").toString())
        );
    }
}
