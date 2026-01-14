package com.kutay.exchange.modules.ledger.internal.entry.model;

import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.transaction.model.Transaction;
import com.kutay.exchange.modules.ledger.internal.entry.model.enums.EntryDirection;
import com.kutay.exchange.modules.ledger.internal.entry.model.enums.EntryLayer;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Entry extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Internal constructor
    protected Entry(Account account,
                    BigDecimal amount,
                    EntryDirection direction,
                    EntryLayer layer,
                    Transaction transaction) {
        this.account = account;
        this.amount = amount;
        this.direction = direction;
        this.layer = layer;
        this.transaction = transaction;
    }


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

    // expose factory methods for debit and credit. (direction is not arbitrary, misuse is much harder)
    public static Entry debit(Account account,
                              Transaction transaction,
                              BigDecimal amount,
                              EntryLayer layer) {
        return new Entry(account, amount, EntryDirection.DEBIT, layer, transaction);
    }

    public static Entry credit(Account account,
                               Transaction transaction,
                               BigDecimal amount,
                               EntryLayer layer) {
        return new Entry(account, amount, EntryDirection.CREDIT, layer, transaction);
    }

    public void markSettled() {
        this.settled = true;
    }
}
