package com.banking.util;

public class Validator {

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

}
