package com.kutay.exchange.modules.Wallet.domain.model;

import com.kutay.exchange.modules.Wallet.domain.model.enums.WalletStatus;
import com.kutay.exchange.modules.Wallet.domain.model.enums.WalletType;
import com.kutay.exchange.shared.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "wallets")
@Getter
@Setter
public class WalletEntity extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "wallets_id_generator")
    @SequenceGenerator(name = "wallets_id_generator",
            sequenceName = "wallet_id_seq")
    private Long id;

    public WalletEntity() {
    }

    public WalletEntity(Long customerId, WalletType walletType) {
        this.customerId = Objects.requireNonNull(customerId);
        this.walletType = Objects.requireNonNull(walletType);
        this.walletStatus = WalletStatus.ACTIVE;
    }

    @Column(nullable = false, updatable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletStatus walletStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType walletType;

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
