package com.kutay.exchange.modules.Wallet.DTO;

import com.kutay.exchange.modules.Wallet.Model.enums.WalletType;

public record WalletRequest(Long customerId, WalletType walletType) {
}
