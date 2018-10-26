package com.banking.account;

import com.banking.account.exceptions.AccountServiceException;
import com.banking.util.Validator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The purpose of this service is store the bank accounts and extract founds from them
 */
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    private Map<String, BigDecimal> accounts = new HashMap<>();

    public AccountServiceImpl() {
        this.accounts.put("01001", BigDecimal.valueOf(2738.59));
        this.accounts.put("01002", BigDecimal.valueOf(23.00));
        this.accounts.put("01003", BigDecimal.valueOf(0.00));
    }

    @Override
    public BigDecimal checkBalance(final String accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public void withdrawAmount(final String accountNumber, final BigDecimal amount) {
        Validator.checkNotNull(accountNumber, "Account number cannot be null");
        if (accounts.get(accountNumber) == null) throw new AccountServiceException(String.format("The account number %s does not exists", accountNumber));
        final BigDecimal savings = accounts.get(accountNumber).subtract(amount);
        if (BigDecimal.valueOf(0).compareTo(savings) == 1) {
            logger.error(String.format("Insufficient founds in account %s to withdraw %s", accountNumber, amount));
            throw new AccountServiceException(String.format("Insufficient founds in account %s to withdraw %s", accountNumber, amount));
        }
        else
            accounts.replace(accountNumber, savings);
    }
}
