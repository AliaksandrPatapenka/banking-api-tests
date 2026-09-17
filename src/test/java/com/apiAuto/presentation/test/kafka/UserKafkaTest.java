package com.apiAuto.presentation.test.kafka;

import com.apiAuto.common.config.HttpStatus;
import com.apiAuto.common.config.KafkaConfig;
import com.apiAuto.common.helpers.JsonContext;
import com.apiAuto.common.helpers.KafkaHelper;
import com.apiAuto.presentation.dto.CreateUserDto;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserKafkaTest {
    private static final String TOPIC_USER_EVENTS = "client-topic";

    @BeforeAll
    static void dbCleanup() {
        PresentationDbCleanup.deleteUsers();
        PresentationDbCleanup.deleteAccounts();
    }

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("UserKafkaEvent. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTest {
        @Test
        @DisplayName("Case 3.1 Event Kafka: Пользователь создан")
        void userCreateKafkaEvent() {
            CreateUserDto requestBody = UserTemplate.defaultRequestBody();
            UserTemplate.createUser(requestBody, HttpStatus.OK);

            String eventJson = KafkaHelper.oneByKeyString(
                    TOPIC_USER_EVENTS,
                    requestBody.getLogin(),
                    KafkaConfig.EVENT_WAIT_TIMEOUT
            );

            Map<String, Object> event = JsonContext.toMap(eventJson);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> eventData = (List<Map<String, Object>>) event.get("eventData");

            Map<String, Object> user = eventData.get(0);
            assertEquals("Пользователь создан", event.get("eventName"));
            assertEquals(requestBody.getLogin(), user.get("login"));
            assertEquals(requestBody.getName(), user.get("name"));
            assertEquals(requestBody.getAge(), ((Number) user.get("age")).intValue());
            assertEquals(requestBody.getGender(), user.get("gender"));
            assertEquals(requestBody.getHairColor(), user.get("hairColor"));
        }
    }
}

