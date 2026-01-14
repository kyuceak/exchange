package com.kutay.exchange.modules.ledger.web.dto;

import com.kutay.exchange.modules.ledger.internal.entry.model.enums.EntryDirection;
import com.kutay.exchange.modules.ledger.internal.entry.model.enums.EntryLayer;
import com.kutay.exchange.modules.ledger.internal.transaction.model.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


// A ledger transaction has invariants that must be enforced before any business-logic, persistence happens.
public record RecordTransactionRequest(
        @NotNull String referenceId,
        @NotNull TransactionType transactionType,
        String description,
        @NotNull @Size(min = 2) List<EntryLine> entries
) {

    /*
     * Represents a single entry line in a transaction
     * */
    public record EntryLine(@NotNull UUID accountId,
                            @NotNull BigDecimal amount,
                            @NotNull EntryDirection direction,
                            @NotNull EntryLayer layer) {
    }

    // DTO canonical constructor
    // validation to ensure debits = credits
    // do invariant validation
    public RecordTransactionRequest {
        // do validation here --> after jackson deserialized and before entering in to controller

        if (entries == null || entries.size() < 2) {
            throw new IllegalArgumentException("Transaction must contain at least 2 entries.");
        }

        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;

        for (EntryLine entry : entries) {
            if (entry.amount.signum() <= 0) {
                throw new IllegalArgumentException("Transaction amount must be positive");
            }
            if (entry.direction() == EntryDirection.CREDIT) {
                creditAmount = creditAmount.add(entry.amount());
            } else if (entry.direction() == EntryDirection.DEBIT) {
                debitAmount = debitAmount.add(entry.amount());
            }
        }

        if (debitAmount.compareTo(creditAmount) != 0) {
            throw new IllegalArgumentException("Transaction is unbalanced, debit and credit amount must be equal");
        }

    }

}
