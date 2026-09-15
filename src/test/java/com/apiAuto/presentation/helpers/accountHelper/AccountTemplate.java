package com.apiAuto.presentation.helpers.accountHelper;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.presentation.endpoints.AccountEndpoints;
import com.apiAuto.presentation.testData.AccountData;

import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;

public class AccountTemplate {
    public static void createAccount(String userLogin, int httpStatus) {
        ApiSteps.postQuery(requestSpec(),
                AccountEndpoints.ENDPOINT_ACCOUNTS,
                Map.of("userLogin", userLogin),
                httpStatus);
    }

    public static void accountDeposit(int accountId, int httpStatus) {
        ApiSteps.postPatchBody(requestSpec(),
                AccountEndpoints.ENDPOINT_ACCOUNTS_DEPOSIT,
                Map.of("id", accountId),
                AccountData.ACCOUNT_DEPOSIT_MAX,
                httpStatus);
    }
}
