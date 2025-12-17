package com.kutay.exchange.modules.Wallet.Model;


import com.kutay.exchange.modules.Wallet.Model.enums.Asset;
import com.kutay.exchange.modules.Wallet.Model.enums.WithdrawalStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "withdrawals")
public class Withdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "withdraw_id_gen")
    @SequenceGenerator(name = "withdraw_id_gen",
            sequenceName = "withdraw_id_seq")
    private Long id;

    @Column(nullable = false)
    private Long walletId;

    @Enumerated(EnumType.STRING)
    private Asset asset;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal fee;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String network;

    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;

    private Instant createdAt;

    private Instant updatedAt;
}
