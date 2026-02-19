package com.kutay.exchange.modules.wallet.domain.service;

import com.kutay.exchange.modules.wallet.api.WalletAccountSpec;
import com.kutay.exchange.modules.wallet.domain.model.Wallet;
import com.kutay.exchange.modules.wallet.infrastructure.persistence.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    // create wallet
    // delete wallet

    @Transactional
    public Wallet createWalletIfNotExists(WalletAccountSpec spec) {
        return walletRepository
                .findByCustomerIdAndWalletType(spec.customerId(), spec.walletType())

                .orElseGet(() -> {
                    try {
                        Wallet wallet = Wallet.create(spec.customerId(), spec.walletType());
                        return walletRepository.save(wallet);
                    } catch (DataIntegrityViolationException e) {
                        return walletRepository
                                .findByCustomerIdAndWalletType(spec.customerId(), spec.walletType())
                                .orElseThrow(() -> new IllegalStateException("could not return wallet after conflict"));
                    }
                });
    }
}
