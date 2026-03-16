package com.kutay.exchange.modules.ledger.infrastructure.events;

import com.kutay.exchange.modules.ledger.internal.transaction.model.enums.TransactionType;
import com.kutay.exchange.shared.contracts.Asset;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record LedgerTransactionRecorded(
        @NotNull String transactionId,
        @NotNull String walletId,
        @NotNull String asset,
        @NotNull String referenceId,
        @NotNull String transactionType,
        @NotNull @Size(min = 2) List<TransactionEntry> entries,
        Instant occurredAt
) {

    public record TransactionEntry(String entryId,
                                   String accountState,
                                   String direction,
                                   String amount) {
    }

    public static LedgerTransactionRecorded from(
            UUID transactionId,
            UUID walletId,
            Asset asset,
            String referenceId,
            TransactionType transactionType,
            List<TransactionEntry> entries) {

        return new LedgerTransactionRecorded(
                transactionId.toString(),
                walletId.toString(),
                asset.name(),
                referenceId,
                transactionType.name(),
                entries,
                Instant.now()
        );
    }
}
