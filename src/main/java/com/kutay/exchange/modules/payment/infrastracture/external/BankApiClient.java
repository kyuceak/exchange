package com.kutay.exchange.modules.payment.infrastracture.external;

import com.kutay.exchange.modules.payment.domain.models.BankTransfer;

public interface BankApiClient {
    void initiateWithdraw(BankTransfer withdraw) throws WithdrawRejectException;
}
