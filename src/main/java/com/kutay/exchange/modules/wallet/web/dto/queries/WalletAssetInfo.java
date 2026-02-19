package com.kutay.exchange.modules.wallet.web.dto.queries;

import com.kutay.exchange.shared.model.Asset;

import java.math.BigDecimal;

public record WalletAssetInfo(Asset asset,
                              BigDecimal availableBalance,
                              BigDecimal lockedBalance,
                              BigDecimal borrowedAmount,
                              BigDecimal interestowed
) {
}
