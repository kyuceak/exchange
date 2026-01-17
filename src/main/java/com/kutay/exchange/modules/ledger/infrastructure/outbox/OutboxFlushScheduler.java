package com.kutay.exchange.modules.ledger.infrastructure.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxFlushScheduler {
    private static final int BATCH_SIZE = 100;

    private final OutboxResultBuffer outboxResultBuffer;
    private final OutboxRepository outboxRepository;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void flushResults() {
        flushSuccess(); // calls @Modifying
        flushFailed(); // calls @Modifying
    }

    private void flushSuccess() {
        List<UUID> successEvents = outboxResultBuffer.drainSuccess(BATCH_SIZE);

        if (!successEvents.isEmpty()) {
            outboxRepository.markAsSent(new HashSet<>(successEvents), Instant.now());
            log.info("Marked {} events as SENT", successEvents.size());
        }
    }

    private void flushFailed() {
        List<UUID> failedEvents = outboxResultBuffer.drainFailure(BATCH_SIZE);

        if (!failedEvents.isEmpty()) {
            outboxRepository.markAsFailed(new HashSet<>(failedEvents));
            log.info("Marked {} events as FAILED", failedEvents.size());
        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    @Transactional // since its an modifying query it requires an transaction
    public void recoverStuckEvents() {
        Instant threshold = Instant.now().minusSeconds(30);
        int recovered = outboxRepository.resetStuckSendingEvents(threshold);

        if (recovered > 0) {
            log.info("Recovered {} stuck events", recovered);
        }
    }
}
