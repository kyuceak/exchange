package com.kutay.exchange.modules.ledger.infrastructure.persistence;

import com.kutay.exchange.modules.ledger.internal.entry.model.Entry;
import com.kutay.exchange.shared.contracts.Asset;
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

    // Calculate balance from entries ( source of truth )
    @Query("""
            SELECT COALESCE(SUM(e.signedAmount), 0)
            FROM Entry e
            JOIN e.account a
            WHERE a.walletId = :walletId
            AND a.asset = :asset
            """)
    BigDecimal calculateBalance(@Param("walletId") UUID walletId,
                                @Param("asset") Asset asset);


    @Query("""
            SELECT COALESCE(SUM(e.signedAmount), 0)
            FROM Entry e
            JOIN Account a
            WHERE a.id = :accountId
            AND a.asset = :asset
            """)
    BigDecimal calculateBalanceByAccountId(@Param("accountId") UUID accountId,
                                           @Param("asset") Asset asset);
}
