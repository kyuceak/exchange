package com.kutay.exchange.modules.wallet.application.commands;

import com.kutay.exchange.modules.wallet.domain.model.WalletAsset;
import com.kutay.exchange.modules.wallet.infrastructure.persistence.WalletAssetRepository;
import com.kutay.exchange.shared.model.Asset;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletReservationServiceImpl implements WalletReservationService {
    private final WalletAssetRepository walletAssetRepository;
    private final WalletCacheService walletCacheService;

    @Override
    @Transactional
    public void reserveBalance(UUID walletId, Asset asset, BigDecimal amount) {
        WalletAsset walletAsset = walletAssetRepository
                .findByWalletIdAndAsset(walletId, asset)
                .orElseThrow(() -> new EntityNotFoundException("Such walletId and asset does not exist"));

        walletAsset.lockBalance(amount);
        walletCacheService.evictBalance(walletId, asset);

        log.info("Reserved {} {} for walletId={}", amount, asset, walletId);
    }

    @Transactional
    @Override
    public void releaseBalance(UUID walletId, Asset asset, BigDecimal amount) {
        WalletAsset walletAsset = walletAssetRepository.
                findByWalletIdAndAsset(walletId, asset)
                .orElseThrow(() -> new EntityNotFoundException("Such walletId and asset does not exist"));

        walletAsset.unlockBalance(amount);
        walletCacheService.evictBalance(walletId, asset);
        log.info("Released {} {} for walletId={}", amount, asset, walletId);
    }
}
