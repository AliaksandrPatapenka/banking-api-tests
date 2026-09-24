package com.apiAuto.common.config;


import com.apiAuto.common.helpers.PropertiesHelper;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;

/**
 * Конфигурация Kafka: адреса брокеров и таймаут ожидания событий.<br>
 * Метод consumerProps(groupId) собирает свойства для консьюмера.
 */

public final class KafkaConfig {
    private KafkaConfig() {
    }

    public static final String KAFKA_SERVERS = System.getProperty(
            "kafka.servers", PropertiesHelper.props.getProperty("kafka.servers"));

    public static final Duration EVENT_TIMEOUT = Duration.ofSeconds(
            Long.parseLong(System.getProperty(
                    "event.timeout", PropertiesHelper.props.getProperty(
                            "event.timeout"))));

    public static Properties consumerProps(String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", KAFKA_SERVERS);
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", "false");
        return props;
    }
}
