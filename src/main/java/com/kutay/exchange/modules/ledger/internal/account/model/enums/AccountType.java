package com.kutay.exchange.modules.ledger.internal.account.model.enums;

import com.kutay.exchange.shared.contracts.EntryDirection;

import java.math.BigDecimal;

public enum AccountType {
    ASSET, // Exchange treasury
    LIABILITY, // User balances ( bizim borçlarimiz )
    REVENUE,
    EXPENSE;

    public BigDecimal calculateSignedAmount(BigDecimal amount, EntryDirection direction) {
        boolean isNaturalDirection = switch (this) {
            case ASSET, EXPENSE -> direction == EntryDirection.DEBIT;
            case LIABILITY, REVENUE -> direction == EntryDirection.CREDIT;
        };
        return isNaturalDirection ? amount : amount.negate();
    }
}
