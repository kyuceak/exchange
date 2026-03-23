package com.kutay.exchange.modules.payment.infrastracture.messaging.outbox;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class PaymentOutboxResultBuffer {
    private static final int MAX_QUEUE_SIZE = 10000;
    private final ConcurrentLinkedQueue<UUID> successQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<UUID> failureQueue = new ConcurrentLinkedQueue<>();

    public void addSuccess(UUID eventId) {
        if (successQueue.size() <= MAX_QUEUE_SIZE) {
            successQueue.add(eventId);
        }
    }

    public void addFailure(UUID eventId) {
        if (failureQueue.size() <= MAX_QUEUE_SIZE) {
            failureQueue.add(eventId);
        }
    }

    public Set<UUID> drainSuccess(int max) {
        Set<UUID> newResult = new HashSet<>();
        for (int i = 0; i < max; i++) {
            UUID id = successQueue.poll();
            if (id == null) {
                break;
            }
            newResult.add(id);
        }
        return newResult;
    }

    public Set<UUID> drainFailure(int max) {
        Set<UUID> newResult = new HashSet<>();
        for (int i = 0; i < max; i++) {
            UUID id = failureQueue.poll();
            if (id == null) {
                break;
            }
            newResult.add(id);
        }
        return newResult;
    }
}
