package com.kutay.exchange.modules.ledger.internal.account;

import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.model.Asset;

import java.util.Objects;
import java.util.UUID;


// its final class because this class should not be extended and
// no subclass can overrite its methods
// behavior is closed and fixed.
public class AccountCodeGenerator {
    private AccountCodeGenerator() {
    }

    // ------------- SYSTEM ACCOUNTS ------------

    public static String generateSystem(AccountType accountType, Asset asset, SystemAccountPurpose purpose) {
        validate(accountType, asset);
        return String.join(":", accountType.name(), "SYSTEM", purpose.name(), asset.name());
    }

    // ------------- USER ACCOUNTS ----------------

    public static String generateUser(AccountType accountType, Asset asset, UUID walletId) {
        validate(accountType, asset);
        return String.join(":", accountType.name(), "USER", walletId.toString(), asset.name());
    }

    private static void validate(AccountType accountType, Asset asset) {
        Objects.requireNonNull(asset, "asset");
        Objects.requireNonNull(accountType, "asset");
    }
}
