package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.common.helpers.DbUtils;
import com.apiAuto.presentation.models.users.UserCreate;

import java.util.List;
import java.util.Map;
import java.util.Objects;


import static org.assertj.core.api.Assertions.assertThat;

public class UserDbAssert {
    public static void assertDataUser(UserCreate expected) {
        Map<String, Object> user = DbUtils.getRow(
                UserSql.SELECT_USER_BY_LOGIN, expected.getLogin());

        assertThat(user).as("Пользователь %s не найден", expected.getLogin()).isNotNull();
        assertThat(user.get("login")).isEqualTo(expected.getLogin());
        assertThat(user.get("name")).isEqualTo(expected.getName());
        assertThat(((Number) user.get("age")).intValue()).isEqualTo(expected.getAge());
        assertThat(user.get("gender")).isEqualTo(expected.getGender());
        assertThat(user.get("hair_color")).isEqualTo(expected.getHairColor());
    }

    public static void assertFriends(UserCreate expected) {
        Map<String, Object> user = DbUtils.getRow(
                UserSql.SELECT_USER_BY_LOGIN, expected.getLogin());

        long userId = ((Number) Objects.requireNonNull(user).get("id")).longValue();

        List<Map<String, Object>> userFriends = DbUtils.getRows(UserSql.SELECT_FRIENDS_BY_USER_ID, userId);
        List<String> actualLogins = userFriends.stream()
                .map(f -> (String) f.get("friend_login"))
                .toList();

        assertThat(actualLogins)
                .as("Логины друзей пользователя: " + expected.getLogin())
                .containsExactlyInAnyOrderElementsOf(expected.getFriends());
    }
}
