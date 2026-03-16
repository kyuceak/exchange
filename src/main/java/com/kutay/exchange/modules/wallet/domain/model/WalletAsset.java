package com.kutay.exchange.modules.wallet.domain.model;

import com.kutay.exchange.shared.contracts.Asset;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "wallet_assets",
        uniqueConstraints = @UniqueConstraint(name = "uk_walletId_asset",
                columnNames = {"wallet_id", "asset"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletAsset extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    public WalletAsset(Wallet wallet, Asset asset) {
        this.wallet = wallet;
        this.asset = asset;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false, updatable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Asset asset;

    // wallet asset balances for projection/cached ( not source of truth)
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal lockedBalance = BigDecimal.ZERO;

    // For margin wallets
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal borrowedAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal interestOwed = BigDecimal.ZERO;

    private String lastReferenceId;

    @Version
    private Long version;

    public static WalletAsset create(Wallet wallet, Asset asset) {
        Objects.requireNonNull(wallet, "wallet can not be null");
        Objects.requireNonNull(asset, "asset can not be null");

        return new WalletAsset(wallet, asset);
    }

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

    public void debitAvailable(BigDecimal amount) {
        requirePositive(amount);
        wallet.requireActive();
        availableBalance = availableBalance.subtract(amount);
    }

    public void creditAvailable(BigDecimal amount) {
        requirePositive(amount);
        wallet.requireActive();
        availableBalance = availableBalance.add(amount);
    }

    private void requirePositive(BigDecimal amount) {
        // .signum() return the sign of the number
        if (amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException("Amount cant be negative");
    }

    public void markReferenceId(String referenceId) {
        this.lastReferenceId = referenceId;
    }

    public void lockBalance(BigDecimal amount) {
        requirePositive(amount);
        this.lockedBalance = this.lockedBalance.add(amount);
    }

    public void unlockBalance(BigDecimal amount) {
        requirePositive(amount);
        this.lockedBalance = this.lockedBalance.subtract(amount);
    }
}
