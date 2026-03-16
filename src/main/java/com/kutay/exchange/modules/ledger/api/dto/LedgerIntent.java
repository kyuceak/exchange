package com.kutay.exchange.modules.ledger.api.dto;

import com.kutay.exchange.shared.contracts.Asset;
import com.kutay.exchange.modules.ledger.internal.transaction.model.enums.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/*
 * Ledger intent is a request DTO that tells the ledger "what I want to do without knowing the-
 * internal double-entry details.
 * */

public record LedgerIntent(
        @NotNull TransactionType transactionType,
        @NotNull UUID walletId,
        @NotNull Asset asset,
        @NotNull BigDecimal amount,
        @NotNull String referenceId
) {
    public static LedgerIntent reserve(UUID walletId, Asset asset, BigDecimal amount, String referenceId) {
        return new LedgerIntent(TransactionType.RESERVE, walletId, asset, amount, referenceId + ":reserve");
    }

    public static LedgerIntent release(UUID walletId, Asset asset, BigDecimal amount, String referenceId) {
        return new LedgerIntent(TransactionType.RELEASE, walletId, asset, amount, referenceId + ":release");
    }

    public static LedgerIntent settleWithdraw(UUID walletId, Asset asset, BigDecimal amount, String referenceId) {
        return new LedgerIntent(TransactionType.SETTLEMENT_WITHDRAWAL, walletId, asset, amount, referenceId + ":settlement_withdrawal");
    }

    public static LedgerIntent settleDeposit(UUID walletId, Asset asset, BigDecimal amount, String referenceId) {
        return new LedgerIntent(TransactionType.SETTLEMENT_DEPOSIT, walletId, asset, amount, referenceId + ":settlement_deposit");
    }

    public static LedgerIntent deposit(UUID walletId, Asset asset, BigDecimal amount, String referenceId) {
        return new LedgerIntent(TransactionType.DEPOSIT, walletId, asset, amount, referenceId);
    }
}
