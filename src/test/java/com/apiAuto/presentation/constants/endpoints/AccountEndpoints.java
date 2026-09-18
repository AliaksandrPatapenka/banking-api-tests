package com.apiAuto.presentation.constants.endpoints;


public final class AccountEndpoints {
    private AccountEndpoints() {
    }

    public static final String ENDPOINT_ACCOUNTS = "/accounts";
    public static final String ENDPOINT_ACCOUNTS_DEPOSIT = "/accounts/{id}/deposit";
    public static final String ENDPOINT_ACCOUNTS_WITHDRAW = "/accounts/{id}/withdraw";
}
