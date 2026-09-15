package com.apiAuto.presentation.helpers.accountHelper;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.common.helpers.DbAssert;
import com.apiAuto.presentation.endpoints.AccountEndpoints;
import com.apiAuto.presentation.testData.AccountData;

import java.math.BigDecimal;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AccountTemplate {
    public static void createAccount(String userLogin) {
        ApiSteps.postQuery(requestSpec(),
                AccountEndpoints.ENDPOINT_ACCOUNTS,
                Map.of("userLogin", userLogin),
                200);
    }

    public static void accountDeposit(int accountId){
        ApiSteps.postPatchBody(requestSpec(),
                        AccountEndpoints.ENDPOINT_ACCOUNTS_DEPOSIT,
                        Map.of("id", accountId),
                        AccountData.ACCOUNT_DEPOSIT_MAX,
                        200);
    }
}
