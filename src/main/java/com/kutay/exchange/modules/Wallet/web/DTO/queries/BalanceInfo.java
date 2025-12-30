package com.kutay.exchange.modules.Wallet.web.DTO.queries;

import java.math.BigDecimal;

public record BalanceInfo(BigDecimal availableBalance,
                          BigDecimal lockedBalance,
                          BigDecimal totalBalance) {
}
