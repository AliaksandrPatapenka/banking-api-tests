package com.apiAuto.presentation.test.kafka;

import com.apiAuto.common.config.KafkaConfig;
import com.apiAuto.common.constants.KafkaConst;
import com.apiAuto.common.helpers.JsonContext;
import com.apiAuto.common.helpers.KafkaHelper;
import com.apiAuto.presentation.constants.kafka.AccountKafkaConst;
import com.apiAuto.presentation.constants.kafka.TopicKafka;
import com.apiAuto.presentation.helpers.accountHelper.AccountSteps;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountKafkaTest {

    @BeforeAll
    static void dbCleanup() {
        PresentationDbCleanup.deleteUsers();
        PresentationDbCleanup.deleteAccounts();
    }

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
            AccountSteps.AccountContext ctx = AccountSteps.createAccount();

            String eventJson = KafkaHelper.oneByKeyInt(
                    TopicKafka.TOPIC_ACCOUNT_EVENTS,
                    ctx.accountId(),
                    KafkaConfig.EVENT_TIMEOUT
            );

            Map<String, Object> event = JsonContext.toMap(eventJson);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> eventData = (List<Map<String, Object>>) event.get(KafkaConst.EVENT_DATA);

            Map<String, Object> account = eventData.get(0);
            assertEquals(AccountKafkaConst.EVENT_ACCOUNT_CREATE, event.get(KafkaConst.EVENT_NAME));
            assertEquals(ctx.accountId(), account.get(AccountKafkaConst.KEY_ID));
            assertEquals(ctx.userLogin(), account.get(AccountKafkaConst.KEY_USER_LOGIN));
        }

        @Test
        @Order(2)
        @DisplayName("Case 6.2 Event Kafka: Пополнение счёта")
        void accountDeposit() {
            AccountSteps.AccountContext ctx = AccountSteps.createAccount();
            BigDecimal getBalance = AccountSteps.depositAndGetBalance(ctx);

            String eventJson = KafkaHelper.oneByFilter(
                    TopicKafka.TOPIC_ACCOUNT_EVENTS,
                    ctx.accountId(),
                    AccountKafkaConst.EVENT_ACCOUNT_DEPOSIT,
                    KafkaConfig.EVENT_TIMEOUT
            );

            Map<String, Object> event = JsonContext.toMap(eventJson);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> eventData = (List<Map<String, Object>>) event.get(KafkaConst.EVENT_DATA);

            Map<String, Object> account = eventData.get(0);
            BigDecimal balance = new BigDecimal(account.get(AccountKafkaConst.KEY_BALANCE).toString());
            Map<String, Object> transaction = eventData.get(1);
            BigDecimal amount = new BigDecimal(transaction.get(AccountKafkaConst.KEY_AMOUNT).toString());

            assertEquals(AccountKafkaConst.EVENT_ACCOUNT_DEPOSIT, event.get(KafkaConst.EVENT_NAME));
            assertEquals(ctx.accountId(), account.get(AccountKafkaConst.KEY_ID));
            assertEquals(ctx.userLogin(), account.get(AccountKafkaConst.KEY_USER_LOGIN));
            assertEquals(0, balance.compareTo(getBalance), "Баланс в событии не совпадает с БД");
            assertEquals(0, amount.compareTo(getBalance), "Сумма в событии не совпадает с балансом");
        }

        @Test
        @Order(3)
        @DisplayName("Case 6.3 Event Kafka: Снятие со счёта")
        void accountWithdraw() {
            AccountSteps.AccountContext ctx = AccountSteps.createAccount();
            BigDecimal getBalance = AccountSteps.depositAndGetBalance(ctx);
            BigDecimal debitAmount = AccountSteps.withdrawAndGetDebitAmount(ctx, getBalance);
            BigDecimal expectedBalance = getBalance.subtract(debitAmount);


            String eventJson = KafkaHelper.oneByFilter(
                    TopicKafka.TOPIC_ACCOUNT_EVENTS,
                    ctx.accountId(),
                    AccountKafkaConst.EVENT_ACCOUNT_WITHDRAWAL,
                    KafkaConfig.EVENT_TIMEOUT
            );

            Map<String, Object> event = JsonContext.toMap(eventJson);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> eventData = (List<Map<String, Object>>) event.get(KafkaConst.EVENT_DATA);

            Map<String, Object> account = eventData.get(0);
            BigDecimal actualBalance = new BigDecimal(account.get(AccountKafkaConst.KEY_BALANCE).toString());
            Map<String, Object> transaction = eventData.get(1);
            BigDecimal amount = new BigDecimal(transaction.get(AccountKafkaConst.KEY_AMOUNT).toString());

            assertEquals(AccountKafkaConst.EVENT_ACCOUNT_WITHDRAWAL, event.get(KafkaConst.EVENT_NAME));
            assertEquals(ctx.accountId(), account.get(AccountKafkaConst.KEY_ID));
            assertEquals(ctx.userLogin(), account.get(AccountKafkaConst.KEY_USER_LOGIN));
            assertEquals(0, actualBalance.compareTo(expectedBalance), "Баланс в событии не совпадает с ожидаемым");
            assertEquals(0, amount.compareTo(debitAmount), "Сумма списания не совпадает");
        }
    }
}
