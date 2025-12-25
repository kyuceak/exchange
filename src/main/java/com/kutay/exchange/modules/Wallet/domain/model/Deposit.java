package com.kutay.exchange.modules.Wallet.domain.model;


import com.kutay.exchange.modules.Wallet.domain.model.enums.Asset;
import com.kutay.exchange.modules.Wallet.domain.model.enums.DepositStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "deposit_id_gen")
    @SequenceGenerator(name = "deposit_id_gen",
            sequenceName = "deposit_id_seq")
    private Long id;

    @Column(nullable = false)
    private Long walletId;

    @Enumerated(EnumType.STRING)
    private Asset asset;

    @Column(nullable = false)
    private String tx_hash; // transaction hash unique identifier of a blockchain transaciton

    private String network; // MOCK, BTC, ERC20

    @Enumerated(EnumType.STRING)
    private DepositStatus status;

    private Instant createdAt;

    private Instant confirmedAt;
}
