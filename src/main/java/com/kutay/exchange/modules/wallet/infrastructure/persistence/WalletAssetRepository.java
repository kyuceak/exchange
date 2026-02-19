package com.kutay.exchange.modules.wallet.infrastructure.persistence;

import com.kutay.exchange.modules.wallet.domain.model.WalletAsset;
import com.kutay.exchange.shared.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface WalletAssetRepository extends JpaRepository<WalletAsset, Long> {
    /* Opted optimistic concurrency control because since its single user wallet,
     * deposits and withdraw will only come from one single user and I am expecting,
     * low contention on rows. So felt like this is much better for scalability.
     * */

    /// /    @Lock(LockModeType.OPTIMISTIC)
//    @Query("SELECT wa FROM WalletAsset wa WHERE wa.wallet.id =: walletId AND wa.asset =: asset")
//    Optional<WalletAsset> findByWalletIdAndAsset(@Param("walletId") UUID walletId, @Param("asset") Asset asset);

    Optional<WalletAsset> findByWalletIdAndAsset(UUID walletId, Asset asset);


}
