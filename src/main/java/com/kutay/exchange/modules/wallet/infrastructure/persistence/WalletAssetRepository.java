package com.kutay.exchange.modules.wallet.infrastructure.persistence;

import com.kutay.exchange.modules.wallet.domain.model.WalletAsset;
import com.kutay.exchange.shared.contracts.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletAssetRepository extends JpaRepository<WalletAsset, UUID> {
    /* Opted optimistic concurrency control because since its single user wallet,
     * deposits and withdraw will only come from one single user and I am expecting,
     * low contention on rows. So felt like this is much better for scalability.
     * */

    Optional<WalletAsset> findByWalletIdAndAsset(UUID walletId, Asset asset);

    boolean existsByLastReferenceId(String referenceId);

}
