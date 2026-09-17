package com.apiAuto.presentation.helpers.accountHelper;

import com.apiAuto.common.helpers.DbUtils;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountDbAssert {
    public static void assertAccountBalance(String userLogin, BigDecimal expected) {
        BigDecimal actual = (BigDecimal) DbUtils.getValue(AccountSql.SELECT_ACCOUNT_BALANCE, userLogin);
        assertEquals(0, Objects.requireNonNull(actual).compareTo(expected), "Баланс не совпадает");

    }

    public static BigDecimal getAccountBalance(String userLogin) {
        return (BigDecimal) DbUtils.getValue(AccountSql.SELECT_ACCOUNT_BALANCE, userLogin);
    }

    public static int getAccountId(String userLogin) {
        return ((Number) DbUtils.getValue(AccountSql.SELECT_ACCOUNT_ID, userLogin)).intValue();
    }
}
