package com.apiAuto.common.config;

public class HttpStatus {
    private HttpStatus() {}

    // 2xx
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;

    // 4xx
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;

    // 5xx
    public static final int INTERNAL_ERROR = 500;
    public static final int SERVICE_UNAVAILABLE = 503;
}
