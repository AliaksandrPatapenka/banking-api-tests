package com.apiAuto.presentation.constants.kafka;

import com.apiAuto.presentation.dto.CreateUserDto;

public final class UserKafkaConst {
    private UserKafkaConst() {
    }

    public static final String EVENT_USER_CREATED = "Пользователь создан";

    public static final String KEY_LOGIN = CreateUserDto.Fields.login;
    public static final String KEY_NAME = CreateUserDto.Fields.name;
    public static final String KEY_AGE = CreateUserDto.Fields.age;
    public static final String KEY_GENDER = CreateUserDto.Fields.gender;
    public static final String KEY_HAIR_COLOR = CreateUserDto.Fields.hairColor;
}
