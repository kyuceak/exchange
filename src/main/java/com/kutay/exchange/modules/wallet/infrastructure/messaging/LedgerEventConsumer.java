package com.kutay.exchange.modules.wallet.infrastructure.messaging;

import com.kutay.exchange.modules.wallet.application.commands.LedgerEntryEvent;
import com.kutay.exchange.modules.wallet.application.commands.WalletProjectionUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LedgerEventConsumer {
    private final WalletProjectionUpdater projectionUpdater;

    /*
     * @KafkaListener
     * 1. Create a message listener adapter
     * 2. Create a message listener container
     * 3. Subscribe it to Kafka
     * 4. Call my method when messages arrive
     * */
    @KafkaListener(
            topics = "${event-topics.LEDGER_ENTRY_CREATED}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleLedgerEntryCreated(ConsumerRecord<String, Map<String, Object>> consumerRecord) {
        /*
         * ConsumerRecor<K,V>
         * is a kafka client class
         * One message pulled from one partition of a topic
         * it contains message key and value (our message, payload)
         * */
        log.info("Received ledger event: key={}, partition={}",
                consumerRecord.key(), consumerRecord.partition());

        try {
            LedgerEntryEvent event = LedgerEntryEvent.fromPayload(consumerRecord.value());
            projectionUpdater.applyLedgerEntry(event);
        } catch (Exception e) {
            log.error("Failed to process Ledger event: key={}, error={}", consumerRecord.key(), e.getMessage());
        }


    }


}
