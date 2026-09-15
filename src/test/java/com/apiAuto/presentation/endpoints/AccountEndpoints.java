package com.apiAuto.presentation.endpoints;


public final class AccountEndpoints {
    public static final String ENDPOINT_ACCOUNTS = System.getProperty("endpoint.accounts", "/accounts");
    public static final String ENDPOINT_ACCOUNTS_DEPOSIT = System.getProperty("endpoint.accountsDeposit", "/accounts/{id}/deposit");
    public static final String ENDPOINT_ACCOUNTS_WITHDRAW = System.getProperty("endpoint.accountsWithdraw", "/accounts/{id}/withdraw");
}
