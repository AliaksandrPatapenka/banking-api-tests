package com.apiAuto.presentation.helpers.accountHelper;

import com.apiAuto.common.helpers.DbUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountDbAssert {
    public static void assertAccountBalance(String userLogin, int expected){
         int actual = ((Number)DbUtils.getValue(AccountSql.SELECT_ACCOUNT_BALANCE, userLogin)).intValue();
         assertEquals(expected, actual, "Баланс не совпадает");
    }
}
