package com.apiAuto.common.helpers;

import com.apiAuto.common.config.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class KafkaHelper {

    private KafkaHelper() {}

    /**
     * Читает одно сообщение из топика, подходящее под фильтр.
     * Ждёт не дольше timeout. Кидает AssertionError, если ничего не пришло.
     */
    public static String readOneMatching(String topic, Predicate<String> filter, Duration timeout) {
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
                    if (filter.test(record.value())) {
                        return record.value();
                    }
                }
            }

            throw new AssertionError(
                    "В топике '" + topic + "' не найдено сообщение, подходящее под фильтр, за "
                            + timeout.toSeconds() + " сек");
        }
    }

    /** Читает одно сообщение по ключу. */
    public static String readOneByKey(String topic, String key, Duration timeout) {
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
}