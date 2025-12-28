
@ApplicationModule(id = "users",
        displayName = "Auth module",
        allowedDependencies = {"customers", "customers::customer-api", "shared"})

package com.kutay.exchange.modules.auth;

import org.springframework.modulith.ApplicationModule;
