package com.apiAuto.presentation.helpers.testHelper;

import com.apiAuto.helpers.testHelper.DbUtils;

public class DbCleanup {
    public static void deleteUsers() {
        DbUtils.delete("DELETE FROM users WHERE login LIKE ?", "Test_%");
    }

    public static void deleteFriends() {
        DbUtils.delete("DELETE FROM user_friends WHERE user_login LIKE ?", "Test_%");
    }
}