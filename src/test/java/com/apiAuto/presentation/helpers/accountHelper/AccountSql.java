package com.apiAuto.presentation.helpers.accountHelper;

public class AccountSql {
    public static String SELECT_ACCOUNT_COUNT = "SELECT COUNT(*) FROM accounts WHERE user_login like ?";
    public static String SELECT_ACCOUNT_BALANCE = "SELECT balance FROM accounts WHERE user_login like ?";
    public static String DELETE_ACCOUNT_BY_USER_LOGIN = "DELETE FROM accounts WHERE user_login LIKE ?";
}
