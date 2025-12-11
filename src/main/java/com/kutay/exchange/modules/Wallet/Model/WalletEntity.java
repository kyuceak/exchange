package com.kutay.exchange.modules.Wallet.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
@Getter
@Setter
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "wallets_id_generator")
    @SequenceGenerator(name = "wallets_id_generator",
            sequenceName = "wallet_id_seq")
    private Long id;

    @Column(nullable = false)
    private String walledId;

    @Column
    private BigDecimal balance;

    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt;


}
