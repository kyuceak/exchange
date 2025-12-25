package com.kutay.exchange.modules.Wallet.infrastructure.persistence;

import com.kutay.exchange.modules.Wallet.domain.model.LedgerEntry;
import com.kutay.exchange.modules.Wallet.domain.model.enums.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    List<LedgerEntry> findAllByWalletIdAndAsset(Long walletId, Asset asset);

    boolean existsByReferenceId(String referenceId);
}
