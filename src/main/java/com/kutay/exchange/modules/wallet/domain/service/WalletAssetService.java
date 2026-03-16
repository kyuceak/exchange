package com.kutay.exchange.modules.wallet.domain.service;

import com.kutay.exchange.modules.wallet.domain.model.Wallet;
import com.kutay.exchange.modules.wallet.domain.model.WalletAsset;
import com.kutay.exchange.shared.contracts.Asset;
import com.kutay.exchange.modules.wallet.infrastructure.persistence.WalletAssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletAssetService {
    // getBalance
    // addBalanceWithLock
    // substractBalanceWithLock
    //create WalletAsset
    //delete Wallet Asset

    private final WalletAssetRepository walletAssetRepository;

    @Transactional
    public WalletAsset createWalletAssetIfNotExists(Wallet wallet, Asset asset) {
        return walletAssetRepository.findByWalletIdAndAsset(wallet.getId(), asset)
                .orElseGet(() -> {
                    try {
                        WalletAsset walletAsset = WalletAsset.create(wallet, asset);
                        return walletAssetRepository.save(walletAsset);
                    } catch (DataIntegrityViolationException e) {
                        return walletAssetRepository.findByWalletIdAndAsset(wallet.getId(), asset)
                                .orElseThrow(() -> new IllegalStateException("could not find walletAsset after conflict"));
                    }
                });
    }
}
