package com.banking.account;

import java.math.BigDecimal;

public interface AccountService {


    BigDecimal checkBalance(String accountNumber);

    void withdrawAmount(String accountNumber, BigDecimal amount);
}
