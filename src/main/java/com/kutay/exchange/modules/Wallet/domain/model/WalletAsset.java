package com.kutay.exchange.modules.Wallet.domain.model;

import com.kutay.exchange.modules.Wallet.domain.model.enums.Asset;
import com.kutay.exchange.shared.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "wallet_assets",
        uniqueConstraints = @UniqueConstraint(name = "uk_walletId_asset",
                columnNames = {"wallet_id", "asset"}))
@Builder
@Getter
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

    // wallet asset balances for projection/cached ( not source of truth)
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

    public BigDecimal getTotalBalance() {
        return availableBalance.add(lockedBalance);
    }


    public BigDecimal getNetBalance() {
        return getTotalBalance().subtract(borrowedAmount).subtract(interestOwed);
    }

    public void lockBalance(BigDecimal amount) {
        requirePositive(amount);
        if (availableBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient available balance");
        }

        availableBalance = availableBalance.subtract(amount);
        lockedBalance = lockedBalance.add(amount);
    }

    public void unlockBalance(BigDecimal amount) {
        requirePositive(amount);
        if (lockedBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient locked balance");
        }

        availableBalance = availableBalance.add(amount);
        lockedBalance = lockedBalance.subtract(amount);
    }

    public void creditAvailable(BigDecimal amount) {
        requirePositive(amount);
        if (availableBalance.compareTo(amount) < 0)
            throw new IllegalArgumentException("Insufficient Balance");
        availableBalance = availableBalance.add(amount);
    }

    public void debitAvailable(BigDecimal amount) {
        requirePositive(amount);
        availableBalance = availableBalance.subtract(amount);
    }


    private void requirePositive(BigDecimal amount) {
        // .signum() return the sign of the number
        if (amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException("Amount cant be negative");
    }
}
