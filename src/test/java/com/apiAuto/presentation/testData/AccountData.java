package com.apiAuto.presentation.testData;

import java.math.BigDecimal;

public class AccountData {
    public static final BigDecimal ACCOUNT_DEPOSIT_MAX = new BigDecimal(System.getProperty("account.deposit.max", "9999999999999999.99"));
    public static final BigDecimal ACCOUNT_DEPOSIT_MIN = new BigDecimal(System.getProperty("account.deposit.min", "0"));
    public static final BigDecimal ACCOUNT_DEPOSIT_ABOVE_MAX = new BigDecimal(System.getProperty("account.deposit.min", "19999999999999999.99"));
    public static final BigDecimal ACCOUNT_DEPOSIT_BELOW_ZERO = new BigDecimal(System.getProperty("account.deposit.belowZero", "-1"));
    public static final BigDecimal ACCOUNT_DEPOSIT_WITHDRAW = new BigDecimal(System.getProperty("account.deposit.belowZero", "99999"));
}
