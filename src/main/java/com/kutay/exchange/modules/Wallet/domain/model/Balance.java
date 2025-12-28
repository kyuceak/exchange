package com.kutay.exchange.modules.Wallet.domain.model;


import com.kutay.exchange.modules.Wallet.domain.model.enums.Asset;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "balances")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "balance_id_gen")
    @SequenceGenerator(name = "balance_id_gen",
            sequenceName = "balance_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long walletId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Asset asset;

    @Column(nullable = false)
    private BigDecimal available = BigDecimal.ZERO; // BigDecimal for exact decimal values, so that no binary floating numbers

    @Column(nullable = false)
    private BigDecimal locked = BigDecimal.ZERO;

    @Version
    private Long version; // optimistic locking

    public void adjust(BigDecimal deltaAvailable, BigDecimal deltaLocked) {

        BigDecimal newAvailable =
                available.add(deltaAvailable != null ? deltaAvailable : BigDecimal.ZERO);

        BigDecimal newLocked =
                locked.add(deltaLocked != null ? deltaAvailable : BigDecimal.ZERO);

        if (newAvailable.signum() < 0) {
            throw new IllegalStateException("Available balance cannot go below zero");
        }

        if (newLocked.signum() < 0) {
            throw new IllegalStateException("Locked balance cannot go below zero");
        }

        this.available = newAvailable;
        this.locked = locked;

    }

}
