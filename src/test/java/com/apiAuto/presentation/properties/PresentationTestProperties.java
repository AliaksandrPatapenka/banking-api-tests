package com.apiAuto.presentation.properties;

public final class PresentationTestProperties {
    public static final String BASE_URI = System.getProperty("base.url", "http://localhost:8081");

    public static final int HTTP_CONNECTION_TIMEOUT = Integer.parseInt(System.getProperty("http.connection.timeout", "10000"));
    public static final int HTTP_SOCKET_TIMEOUT = Integer.parseInt(System.getProperty("http.socket.timeout", "10000"));
}
