package com.apiAuto.presentation.helpers.testHelper;

import com.apiAuto.common.helpers.DbUtils;

public class PresentationDbCleanup {
    public static void deleteUsers() {
        DbUtils.delete("DELETE FROM users WHERE login LIKE ?", "Test_%");
    }

    public static void deleteFriends() {
        DbUtils.delete("DELETE FROM user_friends WHERE user_login LIKE ?", "Test_%");
    }
}