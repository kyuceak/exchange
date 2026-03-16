package com.kutay.exchange.modules.wallet.infrastructure.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kutay.exchange.shared.contracts.EntryDirection;
import com.kutay.exchange.shared.contracts.Asset;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record LedgerTransactionEvent(
        UUID transactionId,
        UUID walletId,
        Asset asset,
        // BigDecimal amount,
        // EntryDirection direction,
        String referenceId,
        // String accountState,
        List<TransactionEntry> entries,
        Instant occurredAt) {

    public record TransactionEntry(UUID entryId,
                                   String accountState,
                                   EntryDirection direction,
                                   BigDecimal amount) {
    }

    // factory pattern
    public static LedgerTransactionEvent fromPayload(Map<String, Object> payload, ObjectMapper objectMapper) {

        // implement objectMapper for TransactionEntry list from entries.
        List<TransactionEntry> entries = objectMapper.convertValue(payload.get("entries"),
                new TypeReference<List<TransactionEntry>>() {
                }
        );

        return new LedgerTransactionEvent(
                UUID.fromString((String) payload.get("transactionId")),
                UUID.fromString((String) payload.get("walletId")),
                Asset.valueOf((String) payload.get("asset")),
//                new BigDecimal((String) payload.get("amount")),
//                EntryDirection.valueOf((String) payload.get("direction")),
                (String) payload.get("referenceId"),
//                (String) payload.get("accountState"),
                entries,
                java.time.Instant.parse(payload.get("occurredAt").toString())
        );
    }
}
