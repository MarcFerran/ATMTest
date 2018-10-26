package com.banking.account;

import com.banking.account.exceptions.AccountServiceException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class AccountServiceTest {

    @Test
    public void should_check_initial_balances() {
        AccountService accountService = new AccountServiceImpl();
        assertThat(accountService.checkBalance("01001")).isEqualTo(BigDecimal.valueOf(2738.59));
        assertThat(accountService.checkBalance("01002")).isEqualTo(BigDecimal.valueOf(23.00));
        assertThat(accountService.checkBalance("01003")).isEqualTo(BigDecimal.valueOf(0.00));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_if_account_number_is_null() {
        AccountService accountService = new AccountServiceImpl();
        accountService.withdrawAmount(null, BigDecimal.valueOf(10));
    }

    @Test
    public void should_withdraw_from_01001() {
        AccountService accountService = new AccountServiceImpl();
        accountService.withdrawAmount("01001", BigDecimal.valueOf(100.59));
        assertThat(accountService.checkBalance("01001").compareTo(BigDecimal.valueOf(2638))).isEqualTo(0);
    }

    @Test(expected = AccountServiceException.class)
    public void should_fail_if_the_account_number_not_exists() {
        AccountService accountService = new AccountServiceImpl();
        accountService.withdrawAmount("33333", BigDecimal.valueOf(100.59));
    }

    @Test(expected = AccountServiceException.class)
    public void should_fail_when_withdrawing_an_excessive_amount() {
        AccountService accountService = new AccountServiceImpl();
        accountService.withdrawAmount("01001", BigDecimal.valueOf(1000000));
    }
}
