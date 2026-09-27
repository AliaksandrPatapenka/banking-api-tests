package com.apiAuto.presentation.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.List;

/**
 * DTO запроса на создание пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class CreateUserDto {
    private String login;
    private String name;
    private int age;
    private String gender;
    private String hairColor;
    private List<String> friends;
}
