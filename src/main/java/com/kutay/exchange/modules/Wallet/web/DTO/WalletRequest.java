package com.kutay.exchange.modules.Wallet.web.DTO;

import com.kutay.exchange.modules.Wallet.domain.model.enums.WalletType;

public record WalletRequest(Long customerId, WalletType walletType) {
}
