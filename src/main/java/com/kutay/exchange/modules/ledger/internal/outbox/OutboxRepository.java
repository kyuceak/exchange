package com.kutay.exchange.modules.ledger.internal.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByProcessedAtIsNullOrderByCreatedAtAsc();
}
