@ApplicationModule(id = "customers",
        displayName = "Customer Module",
        allowedDependencies = {"users", "shared", "wallet :: wallet-api", "wallet", "wallet :: enums"})
package com.kutay.exchange.modules.customer;

import org.springframework.modulith.ApplicationModule;
