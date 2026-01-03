package com.kutay.exchange.modules.ledger.internal.outbox;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;


import java.time.Instant;
import java.util.Map;
import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId; // ledgerEntryId, like referenceId. events belongs to specific records like entry

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType; // e.g., LedgeEntry

    @Enumerated(EnumType.STRING) // persist this enum as its NAME, not ordinal number
    @Column(name = "event_type", nullable = false)
    private EventType eventType; // LedgerEntryRecorded

//    private String topic; // Domain models must not know infrastructure details

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> payload; // Event DATA AS JSON

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "processed_at", updatable = false)
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
