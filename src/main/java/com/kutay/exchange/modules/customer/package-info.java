@ApplicationModule(id = "customers",
        displayName = "Customer Module",
        allowedDependencies = {"users", "shared"})
package com.kutay.exchange.modules.customer;

import org.springframework.modulith.ApplicationModule;
