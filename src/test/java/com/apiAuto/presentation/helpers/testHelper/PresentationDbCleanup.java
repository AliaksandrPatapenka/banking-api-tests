package com.apiAuto.presentation.helpers.testHelper;

import com.apiAuto.common.helpers.DbUtils;
import com.apiAuto.presentation.helpers.userHelper.UserSql;

public class PresentationDbCleanup {
    public static void deleteUsers() {
        DbUtils.delete(UserSql.DELETE_USER_BY_LOGIN, "Test_%");
    }

    public static void deleteFriends() {
        DbUtils.delete(UserSql.DELETE_FRIENDS_BY_USER_LOGIN, "Test_%");
    }
}