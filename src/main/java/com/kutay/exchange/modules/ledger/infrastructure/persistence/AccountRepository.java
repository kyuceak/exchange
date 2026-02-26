package com.kutay.exchange.modules.ledger.infrastructure.persistence;

import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountScope;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.shared.contracts.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    // find or create Account

    Optional<Account> findByWalletIdAndAsset(UUID walletId, Asset asset);

    // USER ACCOUNT LOOKUP
    // one wallet + one asset -> might have multiple accounts, so we need to be more specific with type and scope
    // example : LIABILITY(user balance), LOCKED(orders), FEE
    Optional<Account> findByWalletIdAndAssetAndAccountTypeAndAccountScope(UUID walletId,
                                                                          Asset asset,
                                                                          AccountType accountType,
                                                                          AccountScope accountScope);

    // SYSTEM ACCOUNT LOOKUP
//    Optional<Account> findByWalletIdAndAssetAndAccountTypeAndCode(UUID walletId,
//                                                                  Asset asset,
//                                                                  AccountType accountType,
//                                                                  String code);
// above is unneeded code is already unique
    Optional<Account> findByCode(String code);

    boolean existsAccountByWalletIdAndAsset(UUID walletId, Asset asset);
}
