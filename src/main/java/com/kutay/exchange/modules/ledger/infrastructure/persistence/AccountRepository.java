package com.kutay.exchange.modules.ledger.infrastructure.persistence;

import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountState;
import com.kutay.exchange.shared.contracts.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    // find or create Account
    Optional<Account> findByWalletIdAndAssetAndState(UUID walletId, Asset asset, AccountState state);

    // SYSTEM ACCOUNT LOOKUP
    Optional<Account> findByCode(String code);

    boolean existsAccountByWalletIdAndAsset(UUID walletId, Asset asset);

    // new unique user account lookup
    List<Account> findByWalletIdAndAsset(UUID walletId, Asset asset);
}
