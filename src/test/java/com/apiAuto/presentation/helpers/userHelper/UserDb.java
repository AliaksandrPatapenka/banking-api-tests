package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.common.helpers.DbUtils;
import com.apiAuto.presentation.dto.CreateUserDto;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserDb {
    public static void assertDataUser(CreateUserDto expected) {
        Map<String, Object> user = DbUtils.getRow(
                UserSql.SELECT_USER_BY_LOGIN, expected.getLogin());

        assertNotNull(user, "Пользователь " + expected.getLogin() + " не найден");
        assertEquals(expected.getLogin(), user.get("login"), "Логин пользователя не совпадает");
        assertEquals(expected.getName(), user.get("name"), "Имя пользователя не совпадает");
        assertEquals(expected.getAge(), user.get("age"), "Возраст пользователя не совпадает");
        assertEquals(expected.getGender(), user.get("gender"), "Пол пользователя не совпадает");
        assertEquals(expected.getHairColor(), user.get("hair_color"), "Цвет волос не совпадает");
    }

    public static void assertFriends(CreateUserDto expected) {
        Map<String, Object> user = DbUtils.getRow(
                UserSql.SELECT_USER_BY_LOGIN, expected.getLogin());

        assertNotNull(user, "Пользователь " + expected.getLogin() + " не найден");
        long userId = ((Number) user.get("id")).longValue();

        List<Map<String, Object>> userFriends = DbUtils.getRows(UserSql.SELECT_FRIENDS_BY_USER_ID, userId);
        List<String> actualLogins = userFriends.stream()
                .map(f -> (String) f.get("friend_login"))
                .toList();

        assertEquals(new HashSet<>(expected.getFriends()),
                new HashSet<>(actualLogins),
                "Логины друзей пользователя: " + expected.getLogin());
    }
}
