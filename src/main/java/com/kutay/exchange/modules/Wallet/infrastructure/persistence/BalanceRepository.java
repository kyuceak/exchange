package com.kutay.exchange.modules.Wallet.infrastructure.persistence;

import com.kutay.exchange.modules.Wallet.domain.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
}
