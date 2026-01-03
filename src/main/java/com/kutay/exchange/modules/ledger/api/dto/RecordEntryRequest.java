package com.kutay.exchange.modules.ledger.api.dto;

import com.kutay.exchange.modules.ledger.internal.model.enums.EntryDirection;
import com.kutay.exchange.modules.ledger.internal.model.enums.EntryLayer;
import com.kutay.exchange.shared.model.Asset;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for recording a ledger entry.
 * Used by other modules (Wallet, Trading) to record transactions.
 *
 * @param walletId    the wallet identifier
 * @param asset       the asset type (BTC, ETH, etc.)
 * @param amount      the transaction amount (positive for credit, negative for debit)
 * @param referenceId unique reference for idempotency (e.g., txHash, tradeId)
 */
public record RecordEntryRequest(
        @NotNull UUID accountId,
        @NotNull UUID walletId,
        @NotNull Asset asset,
        @NotNull BigDecimal amount,
        @NotNull String referenceId,
        @NotNull EntryDirection entryDirection,
        @NotNull EntryLayer entryLayer,
        String description
) {
    /**
     * Compact constructor for validation.
     */
    public RecordEntryRequest {
        if (amount != null && amount.signum() == 0) {
            throw new IllegalArgumentException("Amount cannot be zero");
        }
        if (referenceId != null && referenceId.isBlank()) {
            throw new IllegalArgumentException("ReferenceId cannot be blank");
        }
    }
}
