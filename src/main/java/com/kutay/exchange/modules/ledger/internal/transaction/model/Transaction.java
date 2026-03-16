package com.kutay.exchange.modules.ledger.internal.transaction.model;

import com.kutay.exchange.modules.ledger.internal.transaction.model.enums.TransactionType;
import com.kutay.exchange.modules.ledger.internal.transaction.model.enums.TransactionStatus;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "transactions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_transaction_reference",
                        columnNames = {"reference_id"}
                )
        },
        indexes = {
                @Index(name = "idx_transaction_reference_id", columnList = "reference_id"),
                @Index(name = "idx_transaction_status", columnList = "status"),
                @Index(name = "idx_transaction_posted_at", columnList = "posted_at")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    public Transaction(String referenceId,
                       String description,
                       TransactionType transactionType) {
        this(referenceId, description, transactionType, "{}");
    }

    public Transaction(
            String referenceId,
            String description,
            TransactionType transactionType,
            String metadata) {
        this.referenceId = Objects.requireNonNull(referenceId);
        this.description = description;
        this.metadata = metadata;
        this.postedAt = Instant.now();
        this.status = TransactionStatus.PENDING;
        this.transactionType = transactionType;
    }

    @Column(name = "reference_id", nullable = false, updatable = false)
    private String referenceId;  // Idempotency key (e.g., paymentId, tradeId, crypto txId)

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType; // DEPOSIT, WITHDRAWAL

    @Column(name = "posted_at")
    private Instant postedAt;  // When transaction was finalized

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
}
