package com.kutay.exchange.modules.wallet.infrastructure.persistence;

import com.kutay.exchange.modules.wallet.domain.model.Wallet;
import com.kutay.exchange.modules.wallet.domain.model.enums.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    List<Wallet> findAllByCustomerId(UUID customerId);

    // to fetch all wallet assets with single walletId in a single query
    @Query("SELECT w FROM Wallet w LEFT JOIN FETCH w.assets WHERE w.id = :walletId")
    Optional<Wallet> findByIdWithAssets(@Param("walletId") UUID walletId);

    Optional<Wallet> findByCustomerIdAndWalletType(UUID customerId, WalletType walletType);


}
