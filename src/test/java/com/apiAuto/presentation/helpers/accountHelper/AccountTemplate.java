package com.apiAuto.presentation.helpers.accountHelper;

import com.apiAuto.common.helpers.RequestTemplate;
import com.apiAuto.presentation.conctants.endpoints.AccountEndpoints;
import com.apiAuto.presentation.conctants.queryParam.AccountQueryParam;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;

public class AccountTemplate {
    public static Response createAccount(String userLogin, int httpStatus) {
        return RequestTemplate.postQuery(requestSpec(),
                AccountEndpoints.ENDPOINT_ACCOUNTS,
                Map.of(AccountQueryParam.USER_LOGIN, userLogin),
                httpStatus);
    }

    public static Response accountDeposit(int accountId, BigDecimal deposit, int httpStatus) {
        return RequestTemplate.postPatchBody(requestSpec(),
                AccountEndpoints.ENDPOINT_ACCOUNTS_DEPOSIT,
                Map.of(AccountQueryParam.ID, accountId),
                deposit,
                httpStatus);
    }

    public static Response accountWithdraw(int accountId, BigDecimal startBalance, int httpStatus) {
        return RequestTemplate.postPatchBody(requestSpec(),
                AccountEndpoints.ENDPOINT_ACCOUNTS_WITHDRAW,
                Map.of(AccountQueryParam.ID, accountId),
                startBalance,
                httpStatus);
    }
}
