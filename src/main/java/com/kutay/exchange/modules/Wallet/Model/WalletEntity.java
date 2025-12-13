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

//    @Column(nullable = false)
//    private String walletAddress;

//    @Column
//    private BigDecimal balance; // BigDecimal --> exact decimal values,  not binary approximations like floating point.
//
//    @Column
//    @Enumerated(EnumType.STRING) // enum --> fixed values
//    private Currency currency;

    @Column(nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private WalletType type;

    @Column(nullable = false)
    private LocalDateTime created_at = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updated_at = LocalDateTime.now();


}
