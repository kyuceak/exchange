package com.kutay.exchange.modules.Wallet.domain.model;

import com.kutay.exchange.modules.Wallet.domain.model.enums.Asset;
import com.kutay.exchange.shared.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "wallet_asset_id_gen",
        sequenceName = "wallet_asset_id_seq")
public class WalletAsset extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "wallet_asset_id_gen")
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false, updatable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Asset asset;

    @Column(nullable = false, precision = 19, scale = 8)
    @Builder.Default // so that builder does not ignore default value
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(nullable = false, precision = 19, scale = 8)
    @Builder.Default
    private BigDecimal lockedBalance = BigDecimal.ZERO;

    // For margin wallets
    @Column(nullable = false, precision = 19, scale = 8)
    @Builder.Default
    private BigDecimal borrowedAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 19, scale = 8)
    @Builder.Default
    private BigDecimal interestOwed = BigDecimal.ZERO;

    @Version
    private Long version;

    // get total balance
    // get net balance
    // Lock balance function (when placing orders)
    // unlock balance function (when order is filled or cancelled)

    private BigDecimal getTotalBalance() {
        return availableBalance.add(lockedBalance);
    }

    private BigDecimal getNetBalance() {
        return getTotalBalance().subtract(borrowedAmount).subtract(interestOwed);
    }

    private void lockBalance(BigDecimal amount) {
        if (availableBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient available balance");
        }

        availableBalance = availableBalance.subtract(amount);
        lockedBalance = lockedBalance.add(amount);
    }

    private void unlockBalance(BigDecimal amount) {
        if (lockedBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient locked balance");
        }

        availableBalance = availableBalance.add(amount);
        lockedBalance = lockedBalance.subtract(amount);
    }
}
