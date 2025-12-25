package com.kutay.exchange.modules.Wallet.web.DTO;

import com.kutay.exchange.modules.Wallet.domain.model.enums.WalletStatus;
import com.kutay.exchange.modules.Wallet.domain.model.enums.WalletType;

import java.time.Instant;
import java.util.UUID;

public record WalletResponse(UUID walletId,
                             WalletType walletType,
                             WalletStatus walletStatus,
                             Instant createdAt) {

}
