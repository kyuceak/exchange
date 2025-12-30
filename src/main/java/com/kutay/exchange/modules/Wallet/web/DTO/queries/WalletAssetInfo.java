package com.kutay.exchange.modules.Wallet.web.DTO.queries;

import com.kutay.exchange.modules.Wallet.domain.model.enums.Asset;

import java.math.BigDecimal;

public record WalletAssetInfo(Asset asset,
                              BigDecimal availableBalance,
                              BigDecimal lockedBalance,
                              BigDecimal borrowedAmount,
                              BigDecimal interestowed
) {
}
