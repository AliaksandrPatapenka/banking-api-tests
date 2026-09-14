package com.apiAuto.presentation.helpers.accountHelper;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.common.helpers.DbAssert;
import com.apiAuto.presentation.endpoints.AccountEndpoints;

import java.math.BigDecimal;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;

public class CreateAccountTemplate {
    public static void createAccount(String userLogin) {
        ApiSteps.postQuery(requestSpec(),
                AccountEndpoints.ENDPOINT_ACCOUNTS,
                Map.of("userLogin", userLogin),
                200);

        DbAssert.assertCount(AccountSql.SELECT_ACCOUNT_COUNT, userLogin, 1);
        AccountDbAssert.assertAccountBalance(userLogin, new BigDecimal("0"));
    }
}
