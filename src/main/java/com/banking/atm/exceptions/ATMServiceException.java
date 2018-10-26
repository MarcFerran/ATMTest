package com.banking.atm.exceptions;

public class ATMServiceException extends RuntimeException
{
    public ATMServiceException(String message) {
        super(message);
    }
}
