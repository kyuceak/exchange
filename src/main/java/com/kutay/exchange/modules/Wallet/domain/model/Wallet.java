package com.kutay.exchange.modules.wallet.domain.model;

import com.kutay.exchange.modules.wallet.domain.model.enums.WalletStatus;
import com.kutay.exchange.modules.wallet.domain.model.enums.WalletType;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "wallets",
        indexes = {
                @Index(name = "idx_customerId", columnList = "customer_id"),
                @Index(name = "idx_walletType", columnList = "wallet_type"),
                @Index(name = "idx_walletStatus", columnList = "wallet_status"),
                @Index(name = "idx_customerId_walletType", columnList = "customer_id, wallet_type")},
        uniqueConstraints = {@UniqueConstraint(name = "uk_customerId_walletType",
                columnNames = {"customer_id", "wallet_type"}),
                @UniqueConstraint(name = "uk_walletId_walletType", columnNames = {"customer_id", "wallet_type"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Wallet(UUID customerId, WalletType walletType) {
        this.customerId = Objects.requireNonNull(customerId);
        this.walletType = Objects.requireNonNull(walletType);
        this.walletStatus = WalletStatus.ACTIVE;
    }

    @Column(name = "customer_id", nullable = false, updatable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_status", nullable = false)
    private WalletStatus walletStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_type", nullable = false, updatable = false)
    private WalletType walletType;

    @OneToMany(mappedBy = "wallet",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY,
            orphanRemoval = true) // if removed in set, it will be deleted from db
    private Set<WalletAsset> assets = new HashSet<>();

    @Version
    private Long version;

    public static Wallet create(UUID customerId, WalletType walletType) {
        Objects.requireNonNull(customerId, "customerId can not be null");
        Objects.requireNonNull(walletType, "walletType");
        // might add additonal validations later
        return new Wallet(customerId, walletType);
    }

    public void freeze() {
        if (walletStatus == WalletStatus.CLOSED) {
            throw new IllegalStateException("Closed wallet cannot be frozen");
        } else if (walletStatus == WalletStatus.FROZEN) {
            throw new IllegalStateException("Wallet is already frozen.");
        }
        walletStatus = WalletStatus.FROZEN;
    }

    public void unfreeze() {
        if (walletStatus != WalletStatus.FROZEN) {
            throw new IllegalStateException("Wallet is not frozen");
        }
        walletStatus = WalletStatus.ACTIVE;
    }
}
