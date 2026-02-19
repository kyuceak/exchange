package com.kutay.exchange.modules.wallet.web.dto.queries;

import java.io.Serializable;
import java.math.BigDecimal;

public record BalanceInfo(BigDecimal availableBalance,
                          BigDecimal lockedBalance,
                          BigDecimal totalBalance) implements Serializable {
}
