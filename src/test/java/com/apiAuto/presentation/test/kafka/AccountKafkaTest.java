package com.apiAuto.presentation.test.kafka;

import com.apiAuto.common.config.HttpStatus;
import com.apiAuto.common.config.KafkaConfig;
import com.apiAuto.common.helpers.JsonContext;
import com.apiAuto.common.helpers.KafkaHelper;
import com.apiAuto.presentation.conctants.testData.AccountData;
import com.apiAuto.presentation.helpers.accountHelper.AccountDbAssert;
import com.apiAuto.presentation.helpers.accountHelper.AccountTemplate;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountKafkaTest {
    private static final String TOPIC_ACCOUNT_EVENTS = "account-topic";


    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("AccountKafkaEvent. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTest {
        @Test
        @Order(1)
        @DisplayName("Case 6.1 Event Kafka: Создание счёта")
        void accountCreate() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);
            AccountTemplate.createAccount(userLogin, HttpStatus.OK);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            String eventJson = KafkaHelper.oneByKeyInt(
                    TOPIC_ACCOUNT_EVENTS,
                    accountId,
                    KafkaConfig.EVENT_WAIT_TIMEOUT
            );

            Map<String, Object> event = JsonContext.toMap(eventJson);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> eventData = (List<Map<String, Object>>) event.get("eventData");

            Map<String, Object> account = eventData.get(0);
            assertEquals("Создание счёта", event.get("eventName"));
            assertEquals(accountId, account.get("id"));
            assertEquals(userLogin, account.get("userLogin"));
        }

        @Test
        @Order(2)
        @DisplayName("Case 6.2 Event Kafka: Пополнение счёта")
        void accountDeposit() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);
            AccountTemplate.createAccount(userLogin, HttpStatus.OK);
            int accountId = AccountDbAssert.getAccountId(userLogin);
            AccountTemplate.accountDeposit(accountId,
                    AccountData.ACCOUNT_DEPOSIT_MAX,
                    HttpStatus.OK);

            String eventJson = KafkaHelper.oneByFilter(
                    TOPIC_ACCOUNT_EVENTS,
                    accountId,
                    "Пополнение счёта",
                    KafkaConfig.EVENT_WAIT_TIMEOUT
            );

            Map<String, Object> event = JsonContext.toMap(eventJson);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> eventData = (List<Map<String, Object>>) event.get("eventData");

            Map<String, Object> account = eventData.get(0);
            BigDecimal balance = new BigDecimal(account.get("balance").toString());
            Map<String, Object> transaction = eventData.get(1);
            BigDecimal amount = new BigDecimal(transaction.get("amount").toString());

            assertEquals("Пополнение счёта", event.get("eventName"));
            assertEquals(accountId, account.get("id"));
            assertEquals(userLogin, account.get("userLogin"));
            assertEquals(AccountData.ACCOUNT_DEPOSIT_MAX, balance);
            assertEquals(AccountData.ACCOUNT_DEPOSIT_MAX, amount);
        }
    }
}
