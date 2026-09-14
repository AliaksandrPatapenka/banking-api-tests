package com.apiAuto.presentation.patchs;


/**
 * Patch Users
 */
public final class UsersPatch {
    public static final String ENDPOINT_USERS = System.getProperty("endpoint.users", "/users");
    public static final String ENDPOINT_USERS_BY_LOGIN = System.getProperty("endpoint.users", "/users/{login}");
}
