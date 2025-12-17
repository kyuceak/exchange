package com.kutay.exchange.modules.Wallet.Service;

import com.kutay.exchange.modules.Wallet.DTO.WalletRequest;
import com.kutay.exchange.modules.Wallet.DTO.WalletResponse;

import java.util.List;

public interface WalletService {

    /*
     *   createWallet
     *   findByWalletAddress -- idk
     *   get all wallets
     *   get wallet details
     *   get balances for a specific wallet
     *   get all wallets by customerId
     *   deleteWallet
     **
     * */

    List<WalletResponse> getAllWallets();

    WalletResponse getWallet(Long walletId);

    List<WalletResponse> getWalletsForCustomer(Long customerId);

    WalletResponse createWallet(WalletRequest dto);

    void deleteWallet(Long walletId);

    void validateWalletIsActive(Long walletId);


}

