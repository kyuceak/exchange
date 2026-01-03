package com.kutay.exchange.modules.ledger.internal.model;

import com.kutay.exchange.modules.ledger.internal.model.enums.TransactionStatus;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reference_id", nullable = false, updatable = false)
    private String referenceId;  // Idempotency key (e.g., paymentId, tradeId, crypto txId)

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "posted_at")
    private Instant postedAt;  // When transaction was finalized

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private transient Map<String, Object> metadata;
}
