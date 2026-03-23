package com.kutay.exchange.modules.payment.infrastracture.messaging.outbox;

import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.enums.AggregateType;
import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.enums.EventStatus;
import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.enums.PaymentEventType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(
        name = "payment_outbox_events",
        indexes = {@Index(name = "idx_status_sending_at", columnList = "event_status, sending_at")}
)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentOutboxEvent {
    @Id
    private UUID id;

    public PaymentOutboxEvent(UUID id,
                              String aggregateId,
                              AggregateType aggregateType,
                              PaymentEventType paymentEventType,
                              Map<String, Object> payload) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.paymentEventType = paymentEventType;
        this.payload = payload;
        this.retryCount = 0;
        this.eventStatus = EventStatus.PENDING;
    }

    @Column(nullable = false, updatable = false)
    private String aggregateId; // The ID of the entity this event is about. (links back to the source record)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    // aggregateType --> It's the type of entity that the outbox event belongs to
    // used as routing/categorization key when publishing to kafka.
    // kafka consumers can filter or route messages based on it.
    private AggregateType aggregateType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private PaymentEventType paymentEventType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus eventStatus;

    private int retryCount;

    @JdbcTypeCode(SqlTypes.JSON)
    // tell hibernate to serialize/deserialize this field as JSON when reading, writing to the database
    private Map<String, Object> payload;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    private Instant sendingAt;

    private Instant processedAt;

    @PrePersist
    void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }
}
