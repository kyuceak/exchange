package com.kutay.exchange.modules.Wallet.infrastructure.persistence;

import com.kutay.exchange.modules.Wallet.domain.model.WalletAsset;
import com.kutay.exchange.modules.Wallet.domain.model.enums.Asset;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WalletAssetRepository extends JpaRepository<WalletAsset, Long> {
    /* Opted optimistic concurrency control because since its single user wallet,
     * deposits and withdraw will only come from one single user and I am expecting,
     * low contention on rows. So felt like this is much better for scability.
     * */
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT wa FROM WalletAsset wa WHERE wa.wallet.id =: walletId AND wa.asset =: asset")
    Optional<WalletAsset> findByWalletIdAndAssetWithLock(@Param("walletId") UUID walletId, @Param("asset") Asset asset);

    Optional<WalletAsset> findByWalletIdAndAsset(UUID walletId, Asset asset);


}
