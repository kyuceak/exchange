package com.kutay.exchange.modules.ledger.api.dto;

import com.kutay.exchange.shared.model.Asset;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Response DTO for ledger entry queries.
 * Returned when querying transaction history.
 */
public record LedgerEntryResponse(
        UUID id,
        UUID walletId,
        Asset asset,
        BigDecimal amount,
        String referenceId,
        String description,
        Instant createdAt
) {
}
