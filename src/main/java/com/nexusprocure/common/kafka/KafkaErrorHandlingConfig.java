package com.nexusprocure.common.kafka;

import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedEvent;
import com.nexusprocure.common.event.stockissue.StockIssueApprovedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlingConfig {

    // Error handler for Stock Issue events.
    // Uses the Stock Issue KafkaTemplate to publish failed
    // messages to stock-issue-events.DLT after retries are exhausted.
    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            KafkaTemplate<String, StockIssueApprovedEvent> stockIssueKafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(stockIssueKafkaTemplate,
                        (record, exception) ->
                                new org.apache.kafka.common.TopicPartition(
                                        record.topic() + "-dlt",
                                        record.partition()
                                ));

        FixedBackOff backOff = new FixedBackOff(2000L, 2L);
        return new DefaultErrorHandler(recoverer, backOff);
    }
    // Error handler for Purchase Order events.
    // Uses the Purchase Order KafkaTemplate to publish failed
    // messages to purchase-order-events.DLT after retries are exhausted.
    @Bean
    public DefaultErrorHandler purchaseOrderErrorHandler(
            KafkaTemplate<String, PurchaseOrderApprovedEvent> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate,
                        (record, exception) ->
                                new org.apache.kafka.common.TopicPartition(
                                        record.topic() + "-dlt",
                                        record.partition()
                                ));

        FixedBackOff backOff = new FixedBackOff(2000L, 2L);
        return new DefaultErrorHandler(recoverer, backOff);
    }
}
