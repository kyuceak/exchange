package com.kutay.exchange.modules.wallet.api;

import com.kutay.exchange.modules.wallet.domain.model.enums.WalletType;

import java.util.UUID;

public record WalletAccountSpec(UUID customerId, WalletType walletType) {
}
