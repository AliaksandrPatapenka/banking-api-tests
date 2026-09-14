package com.apiAuto.presentation.endpoints;


public final class UsersEndpoints {
    public static final String ENDPOINT_USERS = System.getProperty("endpoint.users", "/users");
    public static final String ENDPOINT_USERS_BY_LOGIN = System.getProperty("endpoint.users", "/users/{login}");
}
