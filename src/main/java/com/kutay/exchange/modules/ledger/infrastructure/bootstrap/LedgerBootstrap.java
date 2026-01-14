package com.kutay.exchange.modules.ledger.infrastructure.bootstrap;

import com.kutay.exchange.modules.ledger.internal.account.LedgerAccountFactory;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.model.Asset;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LedgerBootstrap {

    private final LedgerAccountFactory ledgerAccountFactory;

    @PostConstruct
    private void createSystemAccounts() {
        for (Asset asset : Asset.values()) {

            // create system test accounts for each purpose
            // 1. CRYPTO HOLDINGS
            ledgerAccountFactory.createSystemAccount(asset, AccountType.ASSET, SystemAccountPurpose.CRYPTO_HOLDINGS);

            // 2. TRADING FEES (REVENUE)
            ledgerAccountFactory.createSystemAccount(asset, AccountType.REVENUE, SystemAccountPurpose.TRADING_FEES);

            // 3. Withdrawal Fees (REVENUE)
            ledgerAccountFactory.createSystemAccount(asset, AccountType.REVENUE, SystemAccountPurpose.WITHDRAWAL_FEES);

            // 4. Adjustments
            ledgerAccountFactory.createSystemAccount(asset, AccountType.EXPENSE, SystemAccountPurpose.ADJUSTMENTS);

        }
    }

}
