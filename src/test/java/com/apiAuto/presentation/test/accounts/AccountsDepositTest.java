package com.apiAuto.presentation.test.accounts;

import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import org.junit.jupiter.api.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class AccountsDepositTest {

    @BeforeEach
    void dbCleanup() {
        PresentationDbCleanup.deleteUsers();
        PresentationDbCleanup.deleteAccounts();
    }

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */

    @Nested
    @DisplayName("POST /accounts/{id}/deposit. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTests {

    }

    /**
     * ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("GET AccountsDepositTest. NegativeTests")
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class NegativeTests {

    }
}
