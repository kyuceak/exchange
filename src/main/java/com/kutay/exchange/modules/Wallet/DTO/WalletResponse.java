package com.kutay.exchange.modules.Wallet.DTO;

import com.kutay.exchange.modules.Wallet.Model.enums.WalletStatus;
import com.kutay.exchange.modules.Wallet.Model.enums.WalletType;

import java.time.Instant;

public record WalletResponse(Long walletId,
                             WalletType walletType,
                             WalletStatus walletStatus,
                             Instant createdAt) {

}
