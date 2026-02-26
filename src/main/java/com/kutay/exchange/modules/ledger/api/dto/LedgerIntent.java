package com.kutay.exchange.modules.ledger.api.dto;

import com.kutay.exchange.shared.contracts.Asset;
import com.kutay.exchange.shared.contracts.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record LedgerIntent(
        @NotNull TransactionType transactionType,
        @NotNull UUID walletId,
        @NotNull Asset asset,
        @NotNull BigDecimal amount,
        @NotNull String referenceId
) {
}
