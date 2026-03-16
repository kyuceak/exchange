package com.kutay.exchange.modules.wallet.application.commands;

import com.kutay.exchange.shared.contracts.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletCacheService {

    private final CacheManager cacheManager;

    // using cache-aside pattern --> reads populate, writes evict it
    public void evictBalance(UUID walletId, Asset asset) {
        String key = walletId + ":" + asset.name();

        Cache balanceCache = cacheManager.getCache("walletBalance");
        if (balanceCache != null) balanceCache.evict(key);

        Cache availableCache = cacheManager.getCache("availableBalance");
        if (availableCache != null) availableCache.evict(key);
    }
}
