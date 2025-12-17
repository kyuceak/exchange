package com.kutay.exchange.modules.Wallet.Model;


import com.kutay.exchange.modules.Wallet.Model.enums.Asset;
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

    @Column(nullable = false)
    private Long walletId;

    @Enumerated(EnumType.STRING)
    private Asset asset;

    @Column(nullable = false)
    private BigDecimal available = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal locked = BigDecimal.ZERO;
}
