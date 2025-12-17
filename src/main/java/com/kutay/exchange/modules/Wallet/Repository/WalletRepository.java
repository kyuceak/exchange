package com.kutay.exchange.modules.Wallet.Repository;

import com.kutay.exchange.modules.Wallet.Model.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    List<WalletEntity> findAllByCustomerId(Long customerId);
}
