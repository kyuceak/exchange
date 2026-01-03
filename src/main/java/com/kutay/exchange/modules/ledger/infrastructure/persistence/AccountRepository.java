package com.kutay.exchange.modules.ledger.infrastructure.persistence;

import com.kutay.exchange.modules.ledger.internal.model.Account;
import com.kutay.exchange.shared.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    // find or create Account

    // Spring JPA supports both 1. wallet.id or 2. walletId fields.
    // if I had wallet relationship then it would look for wallet.id
    // but since I dont have it instead a field it does 2.walletId field.
    Optional<Account> findByWalletIdAndAsset(UUID walletId, Asset asset);

}
