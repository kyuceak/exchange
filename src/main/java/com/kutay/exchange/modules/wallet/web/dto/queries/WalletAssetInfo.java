package com.kutay.exchange.modules.wallet.web.dto.queries;

import com.kutay.exchange.shared.contracts.Asset;

import java.math.BigDecimal;

public record WalletAssetInfo(Asset asset,
                              BigDecimal availableBalance,
                              BigDecimal lockedBalance,
                              BigDecimal borrowedAmount,
                              BigDecimal interestowed
) {
}
