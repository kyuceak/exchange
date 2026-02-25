package com.kutay.exchange.modules.payment.domain.models;

import com.kutay.exchange.modules.payment.domain.models.enums.Direction;
import com.kutay.exchange.modules.payment.domain.models.enums.PaymentMethod;
import com.kutay.exchange.modules.payment.domain.models.enums.PaymentStatus;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import com.kutay.exchange.shared.model.Asset;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/* current no back-reference from payment to details,
 * only from details to payment is reachable.
 * this is the current approach, later I might change.
 * */

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Payment extends AbstractBaseEntity {
    private static final int MAX_RETRIES = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(updatable = false, nullable = false)
    private UUID walletId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Asset asset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(unique = true, nullable = false, updatable = false)
    private String referenceId; // idempotency key

    @Column()
    private int retryCount = 0;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;

    protected Payment(UUID walletId,
                      Direction direction,
                      Asset asset,
                      PaymentMethod paymentMethod,
                      BigDecimal amount) {
        this.walletId = walletId;
        this.direction = direction;
        this.asset = asset;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = PaymentStatus.NOT_STARTED;
        this.referenceId = UUID.randomUUID().toString();
    }

    public void incrementRetry() {
        this.retryCount++;
        this.nextRetryAt = Instant.now().plusSeconds(retryDelay());
    }

    private long retryDelay() {
        return (long) Math.pow(2, retryCount) * 60; // exponential backoff in seconds
    }

    public boolean isRetryExhausted() {
        return retryCount >= MAX_RETRIES;
    }
}
