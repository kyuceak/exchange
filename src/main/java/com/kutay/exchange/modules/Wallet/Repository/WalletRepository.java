package com.kutay.exchange.modules.Wallet.Repository;

import com.kutay.exchange.modules.Wallet.Model.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
}
