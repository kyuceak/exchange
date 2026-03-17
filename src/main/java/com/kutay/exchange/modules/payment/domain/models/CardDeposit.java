package com.kutay.exchange.modules.payment.domain.models;

import com.kutay.exchange.modules.payment.domain.models.enums.FiatState;
import com.kutay.exchange.shared.contracts.Asset;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Math.pow;

@Entity
@Table(name = "cardPayments")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CardDeposit extends AbstractBaseEntity {
    private static final int MAX_RETRIES = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private UUID walletId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Asset asset;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private String cardLastFour;

    @Column(nullable = false, updatable = false)
    private String cardBrand;

    @Column()
    private String gatewayPaymentId;

    @Column(nullable = false)
    private String referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FiatState state;

    private int retryCount;

    private Instant nextRetryAt;

    public CardDeposit(UUID walletId,
                       Asset asset,
                       BigDecimal amount,
                       String cardLastFour,
                       String cardBrand
    ) {
        this.walletId = walletId;
        this.asset = asset;
        this.amount = amount;
        this.cardLastFour = cardLastFour;
        this.cardBrand = cardBrand;
        this.state = FiatState.CREATED;
        this.referenceId = UUID.randomUUID().toString();
        this.retryCount = 0;
    }

    public static CardDeposit create(UUID walletId,
                                     Asset asset,
                                     BigDecimal amount,
                                     String cardLastFour,
                                     String cardBrand) {
        Objects.requireNonNull(walletId, "walletId must not be null");
        Objects.requireNonNull(asset, "asset must not be null");
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(cardLastFour, "cardLastFour must not be null");
        Objects.requireNonNull(cardBrand, "cardBrand must not be null");
        return new CardDeposit(walletId, asset, amount, cardLastFour, cardBrand);
    }

    // --- State Transitions ---------

    /**
     * CREATED → PENDING_PROVIDER
     * Called when we send the charge request to the gateway.
     */
    public void sendToProvider() {
        assertState(FiatState.CREATED, "send to provider");
        this.state = FiatState.PENDING_PROVIDER;
    }

    /**
     * PENDING_PROVIDER → AUTHORIZED
     * Called when the gateway confirms the bank approved the charge.
     * Money is reserved but not yet captured.
     */
    public void authorize() {
        assertState(FiatState.PENDING_PROVIDER, "authorize");
        this.state = FiatState.AUTHORIZED;
    }

    /**
     * AUTHORIZED → SETTLED
     * Called when money has actually arrived in our account.
     */
    public void settled(String gatewayPaymentId) {
        this.assertState(FiatState.AUTHORIZED, "");
        Objects.requireNonNull(gatewayPaymentId, "gatewayPaymentId must not be null");
        this.state = FiatState.SETTLED;
    }

    /**
     * PENDING_PROVIDER | AUTHORIZED → DECLINED
     * Called when the bank rejects, gateway times out, or capture fails.
     */
    public void decline() {
        if (this.state != FiatState.PENDING_PROVIDER &&
                this.state != FiatState.AUTHORIZED) {
            throw new IllegalStateException("Cannot decline deposit in " + state + " state");
        }
        this.state = FiatState.DECLINED;
    }

    public void incrementRetry() {
        if (state != FiatState.CREATED && state != FiatState.PENDING_PROVIDER) {
            throw new IllegalStateException("Cannot retry deposit in " + state + " state");
        }
        if (isRetryExhausted()) {
            throw new IllegalStateException("Max retries exhausted");
        }
        this.retryCount++;
        this.nextRetryAt = Instant.now().plusSeconds(retryDelay());
    }

    public long retryDelay() {
        return (long) Math.pow(2, retryCount) * 60;
    }

    public boolean isRetryExhausted() {
        return retryCount >= MAX_RETRIES;
    }

    public boolean isTerminal() {
        return state == FiatState.SETTLED || state == FiatState.DECLINED;
    }

    private void assertState(FiatState expected, String action) {
        if (this.state != expected) {
            throw new IllegalStateException("Cannot " + action + " deposit in " + state + " state");
        }
    }
}
