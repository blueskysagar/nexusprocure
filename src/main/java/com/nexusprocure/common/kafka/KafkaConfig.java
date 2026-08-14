package com.nexusprocure.common.kafka;

import com.nexusprocure.common.event.stockissue.StockIssueApprovedEvent;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.nexusprocure.common.event.purchaseorder.PurchaseOrderApprovedEvent;
import jakarta.validation.Valid;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
public class KafkaConfig {
    // =========================================================
    // PRODUCER SIDE
    // =========================================================

    // Creates the ProducerFactory.
    //
    // ProducerFactory knows how to create Kafka producers
    // and contains the configuration needed by those producers.
    @Value(
            "${spring.kafka.bootstrap-servers}"
    )
    private String bootstrapServers;
    //It is creating producer factory
    @Bean
    public ProducerFactory<String, PurchaseOrderApprovedEvent> producerFactory(){
        Map<String, Object> config = new HashMap<>();
        // Tell the producer where the Kafka broker is.
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // The Kafka message key will be a String.
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // The Kafka message value is a Java object.
        // JsonSerializer converts the Java object into JSON.
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // Return the ProducerFactory to Spring.
        //
        // This factory can now create Kafka producers
        // using the configuration above.
        return new DefaultKafkaProducerFactory<>(config);
    }
    //Who creates the kafkatemplate
    @Bean
    // Creates the KafkaTemplate.
    //
    // KafkaTemplate is what our application actually uses
    // to publish messages to Kafka.
    //
    // It gets the ProducerFactory because ProducerFactory
    // knows how to create/configure the Kafka producer.
    public KafkaTemplate<String, PurchaseOrderApprovedEvent> kafkaTemplate(ProducerFactory<String, PurchaseOrderApprovedEvent> producerFactory){
        // Return the KafkaTemplate to Spring.
        //
        // KafkaTemplate uses the ProducerFactory
        // to get/create Kafka producers.
        return new KafkaTemplate<>(producerFactory);
    }
    @Bean
    public ProducerFactory<String, StockIssueApprovedEvent> stockIssueProducerFactory(){
        Map<String, Object> config = new HashMap<>();
        // Tell the stock issue producer where kafka is running
        config.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers
        );
        // Kafka message key will be a String.
        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );
        // Convert StockIssueApprovedEvent Java object into JSON.
        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        // Return a ProducerFactory capable of creating
        // producers for StockIssueApprovedEvent.
        return new DefaultKafkaProducerFactory<>(config);

    }
    @Bean
    public KafkaTemplate<String, StockIssueApprovedEvent> stockIssueKafkaTemplate(
            ProducerFactory<String, StockIssueApprovedEvent> stockIssueProducerFactory
    ) {

        // KafkaTemplate uses the Stock Issue ProducerFactory
        // to create/configure the Kafka producer.
        return new KafkaTemplate<>(stockIssueProducerFactory);
    }



    // =========================================================
    // CONSUMER SIDE
    // =========================================================

    // Creates the ConsumerFactory.
    //
    // ConsumerFactory knows how to create/configure
    // Kafka consumers.
    @Bean
    public ConsumerFactory<String, PurchaseOrderApprovedEvent> consumerFactory(){
        Map<String, Object> config = new HashMap<>();
        // Tell the consumer where the Kafka broker is.
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Consumer belongs to this consumer group.
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "purchase-order-group");
        // If there is no previous offset,
        // start reading from the earliest available message.
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        // Return the ConsumerFactory to Spring.
        //
        // This factory can now create Kafka consumers
        // using the configuration above.

        return new DefaultKafkaConsumerFactory<>(config,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(PurchaseOrderApprovedEvent.class))



        );
    }
@Bean
public ConsumerFactory<String, StockIssueApprovedEvent> stockIssueConsumerFactory() {

    Map<String, Object> config = new HashMap<>();

    // Tell the Stock Issue consumer where Kafka is running.
    config.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            bootstrapServers
    );

    // This consumer belongs to the Inventory consumer group.
    config.put(
            ConsumerConfig.GROUP_ID_CONFIG,
            "inventory-group"
    );

    // If this group has no previous offset,
    // start from the earliest available message.
    config.put(
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
            "earliest"
    );

    // Create a ConsumerFactory specifically for
    // StockIssueApprovedEvent messages.
    return new DefaultKafkaConsumerFactory<>(
            config,
            new StringDeserializer(),
            new ErrorHandlingDeserializer<>(new JsonDeserializer<>(StockIssueApprovedEvent.class))

    );
}
    // ConcurrentKafkaListenerContainerFactory creates ListenerContainerFactory,which needs kafka consumer so for that
    //Consumer factory creates. That consumer kafka sits inside any mdoules inside service.
    @Bean
    // Creates the ListenerContainerFactory.
    //
    // @KafkaListener uses this factory behind the scenes.
    //
    // The factory uses the ConsumerFactory to create
    // the actual Kafka consumers.
    public ConcurrentKafkaListenerContainerFactory<String, PurchaseOrderApprovedEvent> kafkaListenerContainerFactory(ConsumerFactory<String, PurchaseOrderApprovedEvent> consumerFactory, DefaultErrorHandler purchaseOrderErrorHandler){

        ConcurrentKafkaListenerContainerFactory<String, PurchaseOrderApprovedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // Give the listener factory the ConsumerFactory.
        factory.setConsumerFactory(consumerFactory);
        // Return the ListenerContainerFactory to Spring.
        //
        // Spring will use this factory to create the
        // infrastructure needed by @KafkaListener.
        factory.setCommonErrorHandler(purchaseOrderErrorHandler);
        // Without this line, failures are handled by Spring's silent default, never reaching your DLT.
        return factory;

    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockIssueApprovedEvent> stockIssueKafkaListenerContainerFactory(ConsumerFactory<String, StockIssueApprovedEvent> stockIssueconsumerFactory, DefaultErrorHandler kafkaErrorHandler){

        // Creates the Kafka listener container for Stock Issue events.
        ConcurrentKafkaListenerContainerFactory<String, StockIssueApprovedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // Tell this listener factory which ConsumerFactory
        // should create consumers for StockIssueApprovedEvent
        factory.setConsumerFactory(stockIssueconsumerFactory);
        // Attach our retry + DLT error-handling policy.
        // If the listener throws an exception, this handler
        // controls retrying and eventually sending the message to the DLT.
        //"If a Kafka message handled by this listener fails, use this error handler."
        //It basically says:

        //"If a Kafka message handled by this listener fails, use this error handler."
        factory.setCommonErrorHandler(kafkaErrorHandler);
      //  "Spring, here is the fully configured factory. Keep it as a Spring bean and use it when creating this Kafka listener."
        return factory;
    }


}
