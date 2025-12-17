package com.kutay.exchange.modules.Wallet.Model;

import com.kutay.exchange.modules.Wallet.Model.enums.Asset;
import com.kutay.exchange.shared.enums.LedgerType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "ledger_entry") // truth
public class LedgerEntry {

    @Id // to mark it as primary key of entity
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "wallet_id_gen") // tells JPA how an ID is generated
    @SequenceGenerator(name = "wallet_id_gen",
            sequenceName = "wallet_id_seq")
    private Long id;

    @Column(nullable = false)
    private Long walletId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private LedgerType type;

    private String referenceId;

    private Instant createdAt = Instant.now();
}
