package com.apiAuto.presentation.test.accounts;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.presentation.endpoints.AccountEndpoints;
import com.apiAuto.presentation.helpers.accountHelper.AccountDbAssert;
import com.apiAuto.presentation.helpers.accountHelper.AccountTemplate;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import com.apiAuto.presentation.testData.AccountData;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class AccountsWithdrawTest {
    public record AccountContext(String userLogin, int accountId){}

    static class TestData{
        private static AccountContext createAccount(){
             String userLogin = UserTemplate.userGetLogin();
             AccountTemplate.createAccount(userLogin);
             int accountId = AccountDbAssert.getAccountId(userLogin);

             return new AccountContext(userLogin, accountId);
        }

        private static BigDecimal getBalance(AccountContext var){
            AccountTemplate.accountDeposit(var.accountId());
            return AccountDbAssert.getAccountBalance(var.userLogin());
        }
    }



    @BeforeEach
    void dbCleanup() {
        PresentationDbCleanup.deleteUsers();
        PresentationDbCleanup.deleteAccounts();
    }

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */

    @Nested
    @DisplayName("POST /accounts/{id}/withdraw. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTests {
        private static final String ACCOUNT_DEPOSIT_SCHEMA = "schemas/presentation/accountSchema/accountWithdrawSchema.json";

        @Test
        @Order(1)
        @DisplayName("Case 2.1: Снятие суммы со счёта при достаточном балансе")
        void createAccount() {
            AccountContext var = TestData.createAccount();

            System.out.println("body = " + AccountData.ACCOUNT_DEPOSIT_WITHDRAW);
            ApiSteps.postPatchBody(requestSpec(),
                            AccountEndpoints.ENDPOINT_ACCOUNTS_WITHDRAW,
                            Map.of("id", var.accountId ),
                            AccountData.ACCOUNT_DEPOSIT_WITHDRAW,
                            200)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_DEPOSIT_SCHEMA));
        }

    }

    /**
     * ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("GET AccountsWithdraw. NegativeTests")
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class NegativeTests {

    }
}
