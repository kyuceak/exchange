package com.kutay.exchange.modules.ledger.infrastructure.outbox;

import com.kutay.exchange.modules.ledger.infrastructure.outbox.enums.EventStatus;
import com.kutay.exchange.modules.ledger.infrastructure.outbox.enums.LedgerEventType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;


import java.time.Instant;
import java.util.Map;
import java.util.UUID;


@Getter
@Entity
@Table(name = "outbox_events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public OutboxEvent(String aggregateId, String aggregateType, LedgerEventType eventType, Map<String, Object> payload) {
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.payload = payload;
        this.eventStatus = EventStatus.PENDING;
    }

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId; // ledgerEntryId, like referenceId. events belongs to specific records like entry

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType; // e.g., LedgerEntry

    @Column(name = "event_type", nullable = false)
    private LedgerEventType eventType; // LedgerEntryRecorded

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status", nullable = false)
    private EventStatus eventStatus;

    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

//    private String topic; // Domain models must not know infrastructure details

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> payload; // Event DATA AS JSON

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "sending_at")
    private Instant sendingAt;

    @PrePersist
    private void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }


    public void markProcessed() {
        this.processedAt = Instant.now();
    }

    // below methods are good for synchronous flows, but can not be used in cross-thread flows
    public void markSending() {
        this.eventStatus = EventStatus.SENDING;
    }

    public void markSent() {
        this.eventStatus = EventStatus.SENT;
    }
}
