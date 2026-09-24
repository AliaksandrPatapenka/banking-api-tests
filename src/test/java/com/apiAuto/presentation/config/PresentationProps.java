package com.apiAuto.presentation.config;

import com.apiAuto.common.helpers.PropertiesHelper;

public final class PresentationProps {
    public static final String BASE_URI = System.getProperty(
            "base.url", PropertiesHelper.props.getProperty("base.url"));

    public static final int HTTP_CONNECTION_TIMEOUT = Integer.parseInt(
            System.getProperty(
                    "http.socket.timeout", PropertiesHelper.props.getProperty(
                            "http.connection.timeout")));

    public static final int HTTP_SOCKET_TIMEOUT = Integer.parseInt(System.getProperty(
            "http.socket.timeout", PropertiesHelper.props.getProperty(
                    "http.socket.timeout")));}
