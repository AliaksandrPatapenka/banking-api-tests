package com.apiAuto.common.config;


import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;

public final class KafkaConfig {
    private KafkaConfig() {
    }

    public static final String BOOTSTRAP_SERVERS = System.getProperty("kafka.bootstrap.servers", "localhost:9092");

    public static final Duration EVENT_WAIT_TIMEOUT = Duration.ofSeconds(Long.parseLong(System.getProperty("kafka.wait.timeout.seconds", "10")));

    public static Properties consumerProps(String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", "false");
        return props;
    }
}
