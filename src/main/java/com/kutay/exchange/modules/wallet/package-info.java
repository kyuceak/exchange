@ApplicationModule(id = "wallet",
        displayName = "Wallet Module",
        allowedDependencies = {"customers", "shared", "customers :: customer-api", "ledger :: ledger-api", "ledger", "ledger :: ledger-dto"})
package com.kutay.exchange.modules.wallet;

import org.springframework.modulith.ApplicationModule;