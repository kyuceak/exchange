package com.kutay.exchange.modules.payment.infrastracture.messaging.outbox;

import com.kutay.exchange.modules.payment.infrastracture.persistence.PaymentOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentOutboxFlushScheduler {
    private static final int BATCH_SIZE = 100;

    private final PaymentOutboxResultBuffer paymentOutboxResultBuffer;
    private final PaymentOutboxRepository paymentOutboxRepository;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void flushResults() {
        flushSuccess();
        flushFailure();
    }

    private void flushSuccess() {
        Set<UUID> successSet = paymentOutboxResultBuffer.drainSuccess(BATCH_SIZE);
        if (!successSet.isEmpty()) {
            paymentOutboxRepository.markAsSent(successSet, Instant.now());
        }
        log.info("Marked {} events as SENT and its {}", successSet.size(), successSet);
    }

    private void flushFailure() {
        Set<UUID> failureSet = paymentOutboxResultBuffer.drainFailure(BATCH_SIZE);
        if (!failureSet.isEmpty()) {
            paymentOutboxRepository.markAsFailed(failureSet);
            log.info("Marked {} events as FAILED", failureSet.size());
        }
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void recoverStuckEvents() {
        Instant threshold = Instant.now().minusSeconds(60);
        int recovered = paymentOutboxRepository.resetStuckSendingEvents(threshold);

        if (recovered > 0) {
            log.info("Recovered {} stuck events", recovered);
        }
    }
}
