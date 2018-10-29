package com.banking.util;

import java.util.Collection;

public class Validator {

    public static <T> T checkNotNull(final T reference, final Object errorMessage) {
        if (reference == null) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

    public static void checkNotEmpty(final Object instance, final String propertyName) {
        if ((instance == null) ||
                (instance instanceof String && ((String) instance).length() == 0) ||
                (instance instanceof Collection && ((Collection) instance).size() == 0))
            throw new IllegalArgumentException(String.format("A not empty %s is expected", propertyName));
    }

}
