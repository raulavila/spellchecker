package com.raulavila.spellchecker.util;

import com.raulavila.spellchecker.exceptions.MissingArgumentException;

public class Validator {

    public static final String EXCEPTION_EMPTY_PARAMETER = "The parameter %s is empty or null, you must provide a valid value";

    public void validateParam(String paramName, String paramValue) {
        if (paramValue == null || paramValue.isEmpty())
            throw new MissingArgumentException(String.format(EXCEPTION_EMPTY_PARAMETER, paramName));
    }
}
