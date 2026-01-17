package com.kutay.exchange.modules.ledger.infrastructure.outbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * in-memory buffer to store the results of asynchronous kafka sends
 * multiple kafka producer threads adds elements to this queue,
 * and one scheduler reads it. this will cause concurrency problem,
 * so concurrency needs to be handled. --> ConcurrentLinkedQueue
 * */

@Component
@Slf4j
public class OutboxResultBuffer {
    private static final int MAX_QUEUE_SIZE = 10000;

    private final Queue<UUID> successQueue = new ConcurrentLinkedQueue<>();
    private final Queue<UUID> failureQueue = new ConcurrentLinkedQueue<>();

    public void addSuccess(UUID id) {
        if (successQueue.size() <= MAX_QUEUE_SIZE) {
            successQueue.add(id);
        } else {
            log.info("Success queue full, must drop {}", id);
        }
    }

    public void addFailure(UUID id) {
        failureQueue.add(id);
    }

    public List<UUID> drainSuccess(int max) {
        List<UUID> result = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            UUID id = successQueue.poll();
            if (id == null) break;
            result.add(id);
        }
        return result;
    }

    public List<UUID> drainFailure(int max) {
        List<UUID> result = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            UUID id = failureQueue.poll();
            if (id == null) break;
            result.add(id);
        }
        return result;
    }
}
