package com.taylorstieff.heavenhr.model.dto;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class ExceptionError {
    private String error;

    public ExceptionError(MessageSource messageSource, String message, Object... args) {
        error = messageSource.getMessage(message,
                args, Locale.ENGLISH);
    }

    public String getError() {
        return error;
    }
}
