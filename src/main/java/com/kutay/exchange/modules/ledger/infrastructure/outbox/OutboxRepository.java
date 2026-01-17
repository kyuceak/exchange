package com.kutay.exchange.modules.ledger.infrastructure.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByProcessedAtIsNullOrderByCreatedAtAsc();

    /*
     * markAsSending
     * marAsFailed
     * MarkAsSent
     * resetStuckSendingEvents
     * */

    // FOR UPDATE --> lock the these rows for writing
    // SKIP LOCKED --> if they are already locked dont wait just skip

    @Query(value = "SELECT * FROM outbox_events " +
            "WHERE event_status IN ('PENDING','FAILED') " +
            "ORDER BY created_at ASC " +
            "LIMIT 100 " +
            "FOR UPDATE SKIP LOCKED",

            nativeQuery = true)
    List<OutboxEvent> findRetryableEvents();


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE OutboxEvent e 
            SET e.eventStatus = 'SENDING',
            e.sendingAt = :now
            WHERE e.id IN :ids
            """)
    void marksAsSending(@Param("ids") Set<UUID> ids, @Param("now") Instant now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE OutboxEvent e
            SET e.eventStatus = 'SENT',
            e.processedAt = :now
            WHERE e.id IN :ids
            """)
    void markAsSent(@Param("ids") Set<UUID> ids, @Param("now") Instant now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE OutboxEvent e
            SET e.eventStatus = 'FAILED',
            e.retryCount = e.retryCount + 1
            WHERE e.id IN :ids
            """)
    void markAsFailed(@Param("ids") Set<UUID> ids);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE OutboxEvent e
            SET e.eventStatus = 'PENDING'
            WHERE e.eventStatus = 'SENDING' AND e.sendingAt < :threshold
            """)
    int resetStuckSendingEvents(@Param("threshold") Instant threshold);
}
