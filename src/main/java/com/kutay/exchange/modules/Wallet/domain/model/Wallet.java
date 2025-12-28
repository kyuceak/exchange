package com.kutay.exchange.modules.Wallet.domain.model;

import com.kutay.exchange.modules.Wallet.domain.model.enums.WalletStatus;
import com.kutay.exchange.modules.Wallet.domain.model.enums.WalletType;
import com.kutay.exchange.shared.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "wallets", indexes = {
        @Index(name = "idx_user_id", columnList = "customer_id"),
        @Index(name = "idx_wallet_type", columnList = "wallet_type"),
        @Index(name = "idx_wallet_status", columnList = "wallet_status"),
        @Index(name = "idx_user_type", columnList = "user_id, wallet_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@SequenceGenerator(name = "wallets_id_generator",
//        sequenceName = "wallet_id_seq")
public class Wallet extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false, updatable = false)
    private Long customerId;

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
