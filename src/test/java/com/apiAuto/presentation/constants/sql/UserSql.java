package com.apiAuto.presentation.constants.sql;

public class UserSql {
    private UserSql() {
    }

    public static final String SELECT_USER_COUNT = "SELECT COUNT(*) FROM users WHERE login = ?";
    public static final String SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";
    public static final String SELECT_FRIENDS_BY_USER_ID = "SELECT * FROM user_friends WHERE user_id = ?";
    public static final String DELETE_USER_BY_LOGIN = "DELETE FROM users WHERE login LIKE ?";
    public static final String DELETE_FRIENDS_BY_USER_LOGIN = "DELETE FROM user_friends WHERE user_login LIKE ?";

}
