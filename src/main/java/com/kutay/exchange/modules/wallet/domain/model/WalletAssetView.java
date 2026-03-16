package com.kutay.exchange.modules.wallet.domain.model;

import com.kutay.exchange.shared.contracts.Asset;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallet_assets",
        indexes = {
                @Index(name = "idx_total_balance", columnList = "availableBalance, lockedBalance")
        })
@Immutable // hibernate optimization never dirty checked
public class WalletAssetView {
    // No @Version, no business methods

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Asset asset;

    @Column(nullable = false)
    private BigDecimal availableBalance;

    @Column(nullable = false)
    private BigDecimal lockedBalance;
}
