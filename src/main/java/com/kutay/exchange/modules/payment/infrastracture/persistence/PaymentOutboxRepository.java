package com.kutay.exchange.modules.payment.infrastracture.persistence;

import com.kutay.exchange.modules.payment.infrastracture.messaging.outbox.PaymentOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PaymentOutboxRepository extends JpaRepository<PaymentOutboxEvent, UUID> {
    @Query(value = """
            SELECT *
            FROM payment_outbox_events
            WHERE event_status IN ('PENDING', 'FAILED')
            LIMIT 100
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<PaymentOutboxEvent> findRetryableEvents();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE PaymentOutboxEvent e
            SET e.eventStatus = 'SENDING',
            e.sendingAt = :now
            WHERE e.id in :ids
            """)
    void markAsSending(@Param("ids") Set<UUID> ids, @Param("now") Instant now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE PaymentOutboxEvent e
            SET e.eventStatus = 'SENT',
            e.processedAt = :now
            WHERE e.id in :ids
            """)
    void markAsSent(@Param("ids") Set<UUID> ids, @Param("now") Instant now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE PaymentOutboxEvent e
            SET e.eventStatus = 'FAILED',
            e.retryCount = e.retryCount + 1
            WHERE e.id in :ids
            """)
    void markAsFailed(@Param("ids") Set<UUID> ids);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE PaymentOutboxEvent e
            SET e.eventStatus = 'PENDING'
            WHERE e.eventStatus = 'SENDING' AND e.sendingAt < :threshold
            """)
    int resetStuckSendingEvents(@Param("threshold") Instant threshold);
}
