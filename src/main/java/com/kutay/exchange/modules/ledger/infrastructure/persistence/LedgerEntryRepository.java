package com.kutay.exchange.modules.ledger.infrastructure.persistence;

import com.kutay.exchange.modules.ledger.internal.entry.model.Entry;
import com.kutay.exchange.modules.ledger.internal.entry.model.enums.EntryLayer;
import com.kutay.exchange.shared.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Repository for ledger entries.
 * Provides methods for recording and querying transaction history.
 */
@Repository
public interface LedgerEntryRepository extends JpaRepository<Entry, UUID> {

    /**
     * Find all entries for a Transaction
     */
    List<Entry> findByTransactionId(UUID transactionId);

    // Find all entries for a wallet
    @Query("""
            SELECT e FROM Entry e
            JOIN e.account a
            WHERE a.walletId = :walletId
            ORDER BY e.createdAt DESC
            """)
    List<Entry> findByWalletId(@Param("walletId") UUID walletId);

    // Find all Entries for a wallet and asset

    @Query("""
            SELECT e FROM Entry e
            JOIN e.account a 
            where a.walletId = :walletId
            AND a.asset = :asset
            ORDER BY e.createdAt DESC
            """)
    List<Entry> findByWalletIdAndAsset(@Param("walletId") UUID walletId,
                                       @Param("asset") Asset asset);

    // Find all Entries by walletId,asset and layer
    @Query("""
            SELECT e FROM Entry e
            JOIN e.account a
            WHERE a.walletId = :walletId
            AND a.asset = :asset
            AND e.layer = :layer
            """)
    List<Entry> findByWalletIdAndAssetAndLayer(@Param("walletId") UUID walletId,
                                               @Param("asset") Asset asset,
                                               @Param("layer") EntryLayer layer);

    // Calculate balance from entries ( source of truth )
    @Query("""
            SELECT COALESCE(
                       SUM(CASE WHEN e.direction = 'CREDIT' THEN e.amount ELSE -e.amount END),0
                       )
            FROM Entry e
            JOIN e.account a
            WHERE a.walletId = :walletId
            AND a.asset = :asset
            AND e.layer = :layer
            AND e.settled = true
            """)
    BigDecimal calculateBalance(@Param("walletId") UUID walletId,
                                @Param("asset") Asset asset,
                                @Param("layer") EntryLayer layer);
}
