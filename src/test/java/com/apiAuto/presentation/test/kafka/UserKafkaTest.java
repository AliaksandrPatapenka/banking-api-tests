package com.apiAuto.presentation.test.kafka;

import com.apiAuto.common.config.KafkaConfig;
import com.apiAuto.common.helpers.JsonContext;
import com.apiAuto.common.helpers.KafkaHelper;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import com.apiAuto.presentation.models.CreateUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserKafkaTest {
    private static final String TOPIC_USER_EVENTS = "client-topic";

    @Test
    @DisplayName("Создание юзера → событие в Kafka")
    void userCreateKafkaEvent() {
        CreateUser createUser = UserTemplate.defaultRequestBody();

        String eventJson = KafkaHelper.readOneByKey(
                TOPIC_USER_EVENTS,
                createUser.getLogin(),
                KafkaConfig.EVENT_WAIT_TIMEOUT
        );

        Map<String, Object> event = JsonContext.toMap(eventJson);

        assertEquals("Пользователь создан", event.get("eventName"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> eventData = (List<Map<String, Object>>) event.get("eventData");
        Map<String, Object> user = eventData.get(0);

        assertEquals(createUser.getLogin(), user.get("login"));
        assertEquals(createUser.getName(), user.get("name"));
        assertEquals(createUser.getAge(), ((Number) user.get("age")).intValue());
        assertEquals(createUser.getGender(), user.get("gender"));
        assertEquals(createUser.getHairColor(), user.get("hairColor"));
    }
}
