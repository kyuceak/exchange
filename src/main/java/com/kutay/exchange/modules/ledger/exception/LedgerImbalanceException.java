package com.kutay.exchange.modules.ledger.exception;

import java.math.BigDecimal;

public class LedgerImbalanceException extends RuntimeException {
    public LedgerImbalanceException(BigDecimal debit, BigDecimal credit) {
        super("Ledger imbalance (debit and credit must be equal): debit=" + debit + ", credit= " + credit);
    }
}
