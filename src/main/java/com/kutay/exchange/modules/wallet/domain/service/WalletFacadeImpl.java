package com.kutay.exchange.modules.wallet.domain.service;

import com.kutay.exchange.modules.wallet.api.WalletAccountSpec;
import com.kutay.exchange.modules.wallet.api.WalletFacade;
import com.kutay.exchange.modules.wallet.application.WalletCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletFacadeImpl implements WalletFacade {
    private final WalletCoreService walletCoreService;

    @Override
    public void createWallet(WalletAccountSpec spec) {
        walletCoreService.createWallet(spec);
    }
}
