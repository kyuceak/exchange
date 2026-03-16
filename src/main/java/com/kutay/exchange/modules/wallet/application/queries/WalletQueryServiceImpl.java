package com.kutay.exchange.modules.wallet.application.queries;

import com.kutay.exchange.modules.wallet.domain.model.Wallet;
import com.kutay.exchange.modules.wallet.domain.model.WalletAsset;
import com.kutay.exchange.shared.contracts.Asset;
import com.kutay.exchange.modules.wallet.infrastructure.persistence.WalletAssetRepository;
import com.kutay.exchange.modules.wallet.infrastructure.persistence.WalletRepository;
import com.kutay.exchange.modules.wallet.mapper.WalletMapper;
import com.kutay.exchange.modules.wallet.web.dto.WalletResponse;
import com.kutay.exchange.modules.wallet.web.dto.queries.BalanceInfo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Implementation of {@link WalletQueryService} for read-model access.
 * All methods are read-only and optimized for query performance.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletQueryServiceImpl implements WalletQueryService {

    private final WalletRepository walletRepository;
    private final WalletAssetRepository walletAssetRepository;
    private final WalletMapper walletMapper = new WalletMapper();

    /*
     * value --> the name of the cache
     * key --> tell how the key will be constructed in SpEL
     * */
    @Override
    @Cacheable(value = "walletBalance", key = "#walletId + ':' + #asset.name()")
    public Optional<BalanceInfo> getBalance(UUID walletId, Asset asset) {
        return walletAssetRepository.findByWalletIdAndAsset(walletId, asset)
                .map(this::toBalanceInfo);
    }

    @Override
    @Cacheable(value = "availableBalance", key = "#walletId + ':' + #asset.name()")
    public BigDecimal getAvailable(UUID walletId, Asset asset) {
        return walletAssetRepository.findByWalletIdAndAsset(walletId, asset)
                .map(WalletAsset::getAvailableBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public Map<Asset, BalanceInfo> getBalances(UUID walletId) {
        Wallet wallet = walletRepository.findByIdWithAssets(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found: " + walletId));

        Map<Asset, BalanceInfo> balances = new EnumMap<>(Asset.class);
        for (WalletAsset walletAsset : wallet.getAssets()) {
            balances.put(walletAsset.getAsset(), toBalanceInfo(walletAsset));
        }
        return balances;
    }

    @Override
    public WalletResponse getWallet(UUID walletId) {
        Wallet wallet = walletRepository.findByIdWithAssets(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found: " + walletId));

        return walletMapper.convertToDTO(wallet);
    }

    @Override
    public List<WalletResponse> getWallets(UUID userId) {
        // Note: The current repository uses Long customerId.
        // This implementation returns all wallets; adjust when userId->customerId mapping is available.
        List<Wallet> wallets = walletRepository.findAll();
        return walletMapper.convertToDtoList(wallets);
    }


    /**
     * Convert a WalletAsset to BalanceInfo DTO.
     */
    private BalanceInfo toBalanceInfo(WalletAsset walletAsset) {
        return new BalanceInfo(
                walletAsset.getAvailableBalance(),
                walletAsset.getLockedBalance(),
                walletAsset.getTotalBalance()
        );
    }
}
