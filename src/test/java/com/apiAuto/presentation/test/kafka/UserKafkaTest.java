package com.apiAuto.presentation.test.kafka;

import com.apiAuto.common.config.KafkaConfig;
import com.apiAuto.common.constants.HttpStatus;
import com.apiAuto.common.helpers.JsonContext;
import com.apiAuto.common.helpers.KafkaHelper;
import com.apiAuto.presentation.constants.kafka.TopicKafka;
import com.apiAuto.presentation.constants.kafka.UserKafkaConst;
import com.apiAuto.presentation.dto.CreateUserDto;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserKafkaTest {

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
        @DisplayName("Case 3.1: Event Kafka - Пользователь создан")
        void userCreateKafkaEvent() {
            CreateUserDto requestBody = UserTemplate.defaultRequestBody();
            UserTemplate.createUser(requestBody, HttpStatus.OK);

            String eventJson = KafkaHelper.oneByKeyString(
                    TopicKafka.TOPIC_USER_EVENTS,
                    requestBody.getLogin(),
                    KafkaConfig.EVENT_WAIT_TIMEOUT
            );

            Map<String, Object> event = JsonContext.toMap(eventJson);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> eventData = (List<Map<String, Object>>) event.get("eventData");

            Map<String, Object> user = eventData.get(0);
            assertEquals(UserKafkaConst.EVENT_USER_CREATED, event.get("eventName"));
            assertEquals(requestBody.getLogin(), user.get(UserKafkaConst.KEY_LOGIN));
            assertEquals(requestBody.getName(), user.get(UserKafkaConst.KEY_NAME));
            assertEquals(requestBody.getAge(), ((Number) user.get(UserKafkaConst.KEY_AGE)).intValue());
            assertEquals(requestBody.getGender(), user.get(UserKafkaConst.KEY_GENDER));
            assertEquals(requestBody.getHairColor(), user.get(UserKafkaConst.KEY_HAIR_COLOR));
        }
    }
}

