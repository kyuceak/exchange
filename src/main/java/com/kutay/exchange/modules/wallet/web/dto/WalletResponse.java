package com.kutay.exchange.modules.wallet.web.dto;

import com.kutay.exchange.modules.wallet.domain.model.enums.WalletStatus;
import com.kutay.exchange.modules.wallet.domain.model.enums.WalletType;

import java.time.Instant;
import java.util.UUID;

public record WalletResponse(UUID walletId,
                             WalletType walletType,
                             WalletStatus walletStatus,
                             Instant createdAt) {

}
