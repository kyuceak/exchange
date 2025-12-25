package com.kutay.exchange.modules.Wallet.domain;

import com.kutay.exchange.modules.Wallet.domain.model.LedgerEntry;
import com.kutay.exchange.modules.Wallet.domain.model.enums.Asset;
import com.kutay.exchange.modules.Wallet.infrastructure.persistence.LedgerEntryRepository;
import com.kutay.exchange.shared.enums.LedgerType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/*
*    Why did the balance change?

    Who changed it?

    Was it deposit, withdrawal, trade, fee, rollback?

    Can I rebuild balances if data is corrupted?

    Can I audit suspicious behavior?
    *  I need ledgerEntry and ledger service to track money.
    *   This is necessary in money systems.
    * Ledger = source of truth
*
* */


@Service
@RequiredArgsConstructor
public class LedgerServiceImpl {

    private final LedgerEntryRepository ledgerEntryRepository;

    @Transactional // --> multiple operations
    public void record(Long walletId, Asset asset, BigDecimal amount, LedgerType ledgerType, String referenceId) {

        // Idempotency guard
        if (ledgerEntryRepository.existsByReferenceId(referenceId)) {
            return;
        }

        LedgerEntry ledgerEntry = new LedgerEntry(amount, asset, referenceId, ledgerType, walletId);

        ledgerEntryRepository.save(ledgerEntry);
    }

    private void validate(Long walletId, Asset asset, BigDecimal amount, LedgerType ledgerType, String referenceId) {
        if (walletId == null) {
            throw new IllegalArgumentException("walletId can not be null.");
        }
        if (asset == null) {
            throw new IllegalArgumentException("asset can not be null.");
        }
        if (amount == null || amount.signum() == 0) {
            throw new IllegalArgumentException("amount can not be 0.");
        }
        if (ledgerType == null) {
            throw new IllegalArgumentException("ledgerType can not be null.");
        }
        if (referenceId == null) {
            throw new IllegalArgumentException("referenceId can not be null.");
        }
    }
}
