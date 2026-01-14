package com.kutay.exchange.modules.ledger.api;

import com.kutay.exchange.modules.ledger.web.dto.RecordTransactionRequest;

import java.util.UUID;

/**
 * Public API for the Ledger module.
 * Other modules (Wallet, Trading, etc.) use this facade to record and query ledger entries.
 * <p>
 * The ledger is the source of truth for all balance changes.
 * Every deposit, withdrawal, trade, and fee is recorded here.
 */
public interface LedgerFacade {

    //    // ==================== Commands ====================
//
//    /**
//     * Record a new ledger entry.
//     * Idempotent: if referenceId already exists, silently returns without creating duplicate.
//     *
//     * @param request the entry details
//     */
    UUID recordGenericTransaction(RecordTransactionRequest request);

//    // ==================== Queries ====================
//
//    /**
//     * Get transaction history for a wallet and asset.
//     *
//     * @param walletId the wallet identifier
//     * @param asset    the asset type
//     * @return list of entries, ordered by createdAt descending
//     */
//    List<LedgerEntryResponse> getHistory(UUID walletId, Asset asset);
//
//    /**
//     * Get transaction history by ledger type.
//     *
//     * @param walletId the wallet identifier
//     * @param type     the ledger type to filter by
//     * @return list of entries, ordered by createdAt descending
//     */
//    List<LedgerEntryResponse> getHistoryByType(UUID walletId, LedgerType type);
//
//    /**
//     * Get transaction history within a date range.
//     *
//     * @param walletId the wallet identifier
//     * @param from     start of date range (inclusive)
//     * @param to       end of date range (inclusive)
//     * @return list of entries, ordered by createdAt descending
//     */
//    List<LedgerEntryResponse> getHistoryByDateRange(UUID walletId, Instant from, Instant to);
//
//    /**
//     * Find a ledger entry by its reference ID.
//     *
//     * @param referenceId the unique reference (e.g., txHash, tradeId)
//     * @return the entry if found
//     */
//    Optional<LedgerEntryResponse> findByReference(String referenceId);
//
//    /**
//     * Check if a reference ID already exists (for idempotency checks).
//     *
//     * @param referenceId the reference to check
//     * @return true if exists
//     */
//    boolean existsByReference(String referenceId);
}
