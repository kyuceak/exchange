package com.kutay.exchange.modules.ledger.infrastructure.bootstrap;

import com.kutay.exchange.modules.ledger.internal.account.LedgerAccountFactory;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountState;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.contracts.Asset;
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
            for (AccountState state : AccountState.values()) {
                ledgerAccountFactory.createSystemLedger(asset, AccountType.ASSET, SystemAccountPurpose.CRYPTO_HOLDINGS, state);
            }


            // 2. TRADING FEES (REVENUE)
            ledgerAccountFactory.createSystemLedger(asset, AccountType.REVENUE, SystemAccountPurpose.TRADING_FEES, AccountState.SETTLED);

            // 3. Withdrawal Fees (REVENUE)
            ledgerAccountFactory.createSystemLedger(asset, AccountType.REVENUE, SystemAccountPurpose.WITHDRAWAL_FEES, AccountState.SETTLED);

            // 4. Adjustments
            ledgerAccountFactory.createSystemLedger(asset, AccountType.EXPENSE, SystemAccountPurpose.ADJUSTMENTS, AccountState.SETTLED);

        }
    }

}
