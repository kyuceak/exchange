package com.kutay.exchange.modules.Wallet.domain.model;

import com.kutay.exchange.modules.Wallet.domain.model.enums.Asset;
import com.kutay.exchange.shared.enums.LedgerType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "ledger_entry") // truth
//@Table(
//        name = "ledger_entries",
//        indexes = {
//                @Index(name = "idx_ledger_wallet_asset", columnList = "walletId,asset"),
//                @Index(name = "idx_ledger_reference", columnList = "referenceId", unique = true)
//        }
//)
public class LedgerEntry {
    @Id // to mark it as primary key of entity
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "wallet_id_gen") // tells JPA how an ID is generated
    @SequenceGenerator(name = "wallet_id_gen",
            sequenceName = "wallet_id_seq")
    private Long id;

    protected LedgerEntry() {
    }

    public LedgerEntry(BigDecimal amount, Asset asset, String referenceId, LedgerType ledgerType, Long walletId) {
        this.amount = amount;
        this.asset = asset;
        this.referenceId = referenceId;
        this.ledgerType = ledgerType;
        this.walletId = walletId;
    }

    @Column(nullable = false, updatable = false)
    private Long walletId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private LedgerType ledgerType;

    // referenceId --> unique --> idempotency
    // referenceId --> txhash veya tradeId --> real life reference, why this record happened
    @Column(nullable = false, updatable = false, unique = true)
    private String referenceId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
