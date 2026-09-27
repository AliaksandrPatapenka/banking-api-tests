package com.apiAuto.presentation.helpers.accountHelper;

import com.apiAuto.common.constants.HttpStatus;
import com.apiAuto.presentation.constants.testData.AccountData;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import io.qameta.allure.Step;


import java.math.BigDecimal;

/**
 * Составные шаги тестов аккаунтов
 */

public class AccountSteps {
    public record AccountContext(String userLogin, int accountId) {
    }

    @Step("Создание пользователя и аккаунт")
    public static AccountContext createAccount() {
        String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);
        AccountTemplate.createAccount(userLogin, HttpStatus.OK);
        int accountId = AccountDb.getAccountId(userLogin);

        return new AccountContext(userLogin, accountId);
    }

    @Step("Пополнение счёта {ctx.accountId} и получение баланса")
    public static BigDecimal depositAndGetBalance(AccountContext ctx) {
        AccountTemplate.accountDeposit(ctx.accountId(),
                AccountData.ACCOUNT_DEPOSIT_MAX,
                HttpStatus.OK);

        return AccountDb.getAccountBalance(ctx.userLogin());
    }

    @Step("Списание со счёта {ctx.accountId}")
    public static BigDecimal withdrawAndGetDebitAmount(AccountContext ctx, BigDecimal balance) {
        BigDecimal debitAmount = PresentationDataGenerator.debitAmount(balance);
        AccountTemplate.accountWithdraw(ctx.accountId(), debitAmount, HttpStatus.OK);

        return debitAmount;
    }
}
