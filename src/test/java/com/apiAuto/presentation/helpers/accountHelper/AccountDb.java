package com.apiAuto.presentation.helpers.accountHelper;

import com.apiAuto.common.helpers.DbUtils;
import com.apiAuto.presentation.constants.sql.AccountSql;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Работа с данными аккаунтов в БД
 */

public class AccountDb {
    public static void assertAccountBalance(String userLogin, BigDecimal expected) {
        BigDecimal actual = (BigDecimal) DbUtils.getValue(AccountSql.SELECT_ACCOUNT_BALANCE, userLogin);
        assertNotNull(actual, "Аккаунт для пользователя " + userLogin + " не найден в БД");
        assertEquals(0, Objects.requireNonNull(actual).compareTo(expected), "Баланс не совпадает");

    }

    public static BigDecimal getAccountBalance(String userLogin) {
        BigDecimal balance = (BigDecimal) DbUtils.getValue(AccountSql.SELECT_ACCOUNT_BALANCE, userLogin);
        assertNotNull(balance, "Баланс не найден для пользователя " + userLogin);

        return balance;
    }

    public static int getAccountId(String userLogin) {
        Object id = DbUtils.getValue(AccountSql.SELECT_ACCOUNT_ID, userLogin);
        assertNotNull(id, "Account ID не найден для пользователя " + userLogin);

        return ((Number) id).intValue();
    }
}
