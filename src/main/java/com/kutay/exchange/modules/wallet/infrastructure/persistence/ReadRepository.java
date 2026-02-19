package com.kutay.exchange.modules.wallet.infrastructure.persistence;

import com.kutay.exchange.modules.wallet.domain.model.WalletAssetView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReadRepository extends JpaRepository<WalletAssetView, UUID> {

}
