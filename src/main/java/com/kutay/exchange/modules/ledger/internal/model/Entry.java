package com.kutay.exchange.modules.ledger.internal.model;

import com.kutay.exchange.modules.ledger.internal.model.enums.EntryDirection;
import com.kutay.exchange.modules.ledger.internal.model.enums.EntryLayer;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "entries",
        indexes = {
                // unnecessary index, in below index since transaction_id is left most index single queries will hit composite index
                //@Index(name = "idx_txid_reference", columnList = "transaction_id"),
                @Index(name = "idx_txid_created_at", columnList = "transaction_id, created_at"),
                @Index(name = "idx_direction", columnList = "direction"),
                @Index(name = "idx_account_id", columnList = "account_id")})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Entry extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // transaction_id and account_id reference must be add
    @ManyToOne(fetch = FetchType.LAZY)// dont load ledger directly, later implement JOIN FETCH
    @JoinColumn(name = "transaction_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_entry_transaction"))
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_entry_account"))
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EntryDirection direction;

    @Column(nullable = false, updatable = false, precision = 19, scale = 8)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private EntryLayer layer;

    @Column(nullable = false)
    private boolean settled = false;

    public void markSettled() {
        this.settled = true;
    }
}
