package com.apiAuto.common.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonContext {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Метод toJson() превращает Java-объект в JSON-строку для отправки в запросе.
     */
    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Метод toJson() превращает Java-строку в JSON-обьект для отправки в запросе.
     */
    public static Map<String, Object> toMap(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON: " + json, e);
        }
    }

    /**
     * Парсит JSON-строку в объект указанного класса.
     */
    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return new ObjectMapper().readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON: " + json, e);
        }
    }
}
