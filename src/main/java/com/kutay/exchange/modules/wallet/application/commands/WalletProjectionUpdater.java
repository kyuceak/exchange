package com.kutay.exchange.modules.wallet.application.commands;

import com.kutay.exchange.modules.wallet.domain.model.WalletAsset;
import com.kutay.exchange.modules.wallet.infrastructure.persistence.WalletAssetRepository;
import com.kutay.exchange.shared.enums.EntryDirection;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletProjectionUpdater {
    private final WalletAssetRepository walletAssetRepository;
    private final WalletCacheService walletCacheService;

    @Transactional
    public void applyLedgerEntry(LedgerEntryEvent event) {
        WalletAsset walletAsset = walletAssetRepository
                .findByWalletIdAndAsset(event.walletId(), event.asset())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "WalleAsset not found: wallet="
                                        + event.walletId() + ", asset=" + event.asset()));

        // idempotency Check
        if (event.referenceId().equals(walletAsset.getLastReferenceId())) {
            log.info("Duplicate event, skipping : {}", event.referenceId());
            return;
        }

        // update balance
        updateBalance(walletAsset, event);
        walletAsset.markReferenceId(event.referenceId());
        walletAssetRepository.save(walletAsset);

        // invalidate cache
        walletCacheService.evictBalance(event.walletId(), event.asset());

        log.info("Applied ledger entry: walletId={}, asset={}, " +
                        "direction={}, amount={}", event.walletId(), event.asset(),
                event.direction(), event.amount());
    }

    private void updateBalance(WalletAsset walletAsset, LedgerEntryEvent event) {
        BigDecimal amount = event.amount();

        switch (event.direction()) {
            case DEBIT -> {
                switch (event.layer()) {
                    case AVAILABLE -> walletAsset.debitAvailable(amount);
                    case LOCKED -> walletAsset.lockBalance(amount);
                }
            }

            case CREDIT -> {
                switch (event.layer()) {
                    case AVAILABLE -> walletAsset.creditAvailable(amount);
                    case LOCKED -> walletAsset.unlockBalance(amount);
                }
            }
        }
    }
}
