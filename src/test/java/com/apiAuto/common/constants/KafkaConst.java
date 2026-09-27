package com.apiAuto.common.constants;

public final class KafkaConst {
    private KafkaConst() {
    }

    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_DATA = "eventData";

    public static final String DEFAULT_KAFKA_TIMEOUT = "10";
    public static final String DEFAULT_KAFKA_SERVER = "localhost:9092";
}
