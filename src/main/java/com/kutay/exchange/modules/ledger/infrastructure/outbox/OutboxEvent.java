package com.kutay.exchange.modules.ledger.infrastructure.outbox;

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
    }

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId; // ledgerEntryId, like referenceId. events belongs to specific records like entry

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType; // e.g., LedgerEntry

    @Column(name = "event_type", nullable = false)
    private LedgerEventType eventType; // LedgerEntryRecorded

//    private String topic; // Domain models must not know infrastructure details

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> payload; // Event DATA AS JSON

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @PrePersist
    private void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    public void markProcessed() {
        this.processedAt = Instant.now();
    }
}
