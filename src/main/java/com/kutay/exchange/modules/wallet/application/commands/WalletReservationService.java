package com.kutay.exchange.modules.wallet.application.commands;

import com.kutay.exchange.shared.model.Asset;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletReservationService {

    // create wallet
    // suspend wallet
    // delete wallet
    // activate wallet
    // lockBalance, unlockBalance --> for trading

    // freezeWallet, unfreezeWallet, closeWallet --> wallet lifecycle.

    void reserveBalance(UUID walletId, Asset asset, BigDecimal amount);

    void releaseBalance(UUID walletId, Asset asset, BigDecimal amount);
}
