package com.banking.atm;

import com.banking.atm.common.Notes;

import java.util.Map;

public interface ATMService {
    void replenish(final Map<Notes, Integer> notes);
    String checkBalance();
    Map<Notes, Integer> withdraw(String accountNumber, Integer amount);
}
