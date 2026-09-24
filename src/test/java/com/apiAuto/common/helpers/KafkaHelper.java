package com.apiAuto.common.helpers;

import com.apiAuto.common.config.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Утилита для чтения сообщений из топиков Kafka
 */

public final class KafkaHelper {

    private KafkaHelper() {
    }

    /**
     * Читает одно сообщение из топика, подходящее под фильтр.
     */
    public static String oneByFilter(String topic, int accountId, String eventName, Duration timeout) {
        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<>(KafkaConfig.consumerProps("test-" + UUID.randomUUID()));

        try (consumer) {
            consumer.assign(
                    consumer.partitionsFor(topic).stream()
                            .map(p -> new TopicPartition(topic, p.partition()))
                            .collect(Collectors.toList())
            );
            consumer.seekToBeginning(consumer.assignment());
            long deadline = System.currentTimeMillis() + timeout.toMillis();

            while (System.currentTimeMillis() < deadline) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> record : records) {
                    if (!String.valueOf(accountId).equals(record.key())) {
                        continue;
                    }

                    Map<String, Object> event = JsonContext.toMap(record.value());
                    if (eventName.equals(event.get("eventName"))) {
                        return record.value();
                    }
                }
            }

            throw new AssertionError(
                    "В топике '" + topic + "' не найдено сообщение, подходящее под фильтр, за "
                            + timeout.toSeconds() + " сек");
        }
    }

    /**
     * Читает одно сообщение по ключу(String).
     */
    public static String oneByKeyString(String topic, String key, Duration timeout) {
        try (KafkaConsumer<String, String> consumer =
                     new KafkaConsumer<>(KafkaConfig.consumerProps("test-" + UUID.randomUUID()))) {

            consumer.assign(consumer.partitionsFor(topic).stream()
                    .map(p -> new TopicPartition(topic, p.partition()))
                    .toList());
            consumer.seekToBeginning(consumer.assignment());

            long deadline = System.currentTimeMillis() + timeout.toMillis();
            while (System.currentTimeMillis() < deadline) {
                for (var record : consumer.poll(Duration.ofSeconds(1))) {
                    if (key.equals(record.key())) return record.value();
                }
            }
            throw new AssertionError("Сообщение с ключом '" + key + "' не найдено за " + timeout.toSeconds() + " сек");
        }
    }

    /**
     * Читает одно сообщение по ключу(int).
     */
    public static String oneByKeyInt(String topic, int key, Duration timeout) {
        try (KafkaConsumer<String, String> consumer =
                     new KafkaConsumer<>(KafkaConfig.consumerProps("test-" + UUID.randomUUID()))) {

            consumer.assign(consumer.partitionsFor(topic).stream()
                    .map(p -> new TopicPartition(topic, p.partition()))
                    .toList());
            consumer.seekToBeginning(consumer.assignment());

            String keyStr = String.valueOf(key);
            long deadline = System.currentTimeMillis() + timeout.toMillis();
            while (System.currentTimeMillis() < deadline) {
                for (var record : consumer.poll(Duration.ofSeconds(1))) {
                    if (keyStr.equals(record.key())) return record.value();
                }
            }
            throw new AssertionError("Сообщение с ключом '" + key + "' не найдено за " + timeout.toSeconds() + " сек");
        }
    }
}