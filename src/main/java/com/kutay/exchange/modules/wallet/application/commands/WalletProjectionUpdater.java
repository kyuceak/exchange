package com.kutay.exchange.modules.wallet.application.commands;

import com.kutay.exchange.modules.wallet.domain.model.WalletAsset;
import com.kutay.exchange.modules.wallet.infrastructure.messaging.LedgerTransactionEvent;
import com.kutay.exchange.modules.wallet.infrastructure.persistence.WalletAssetRepository;
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
    public void applyLedgerEntry(LedgerTransactionEvent event) {
        WalletAsset walletAsset = walletAssetRepository
                .findByWalletIdAndAsset(event.walletId(), event.asset())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "WalletAsset not found: wallet="
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

        log.info("Applied ledger transaction: walletId={}, asset={}, transactionId={}", event.walletId(), event.asset(), event.transactionId());
    }

    private void updateBalance(WalletAsset walletAsset, LedgerTransactionEvent event) {

        for (LedgerTransactionEvent.TransactionEntry entry : event.entries()) {
            BigDecimal amount = entry.amount();

            switch (entry.direction()) {
                case DEBIT -> {
                    switch (entry.accountState()) {
                        case "SETTLED" -> walletAsset.debitAvailable(amount);
                        case "PENDING_DEBIT" -> walletAsset.unlockBalance(amount);
                    }
                }

                case CREDIT -> {
                    switch (entry.accountState()) {
                        case "SETTLED" -> walletAsset.creditAvailable(amount);
                        case "PENDING_DEBIT" -> walletAsset.lockBalance(amount);
                    }
                }
            }
        }
    }
}
