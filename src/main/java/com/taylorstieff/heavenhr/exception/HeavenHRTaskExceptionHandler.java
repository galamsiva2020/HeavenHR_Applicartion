package com.taylorstieff.heavenhr.exception;

import com.taylorstieff.heavenhr.model.dto.ExceptionError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class HeavenHRTaskExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity handleOfferNotFound(OfferNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionError(messageSource,"error.offer_not_found", e.getMissingId()));
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity handleApplicationNotFound(ApplicationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionError(messageSource,"error.application_not_found", e.getMissingId()));
    }

    @ExceptionHandler(InvalidApplicationStatusException.class)
    public ResponseEntity handleInvalidApplicationStatus(InvalidApplicationStatusException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionError(messageSource,"error.application_status_not_valid", e.getBadStatus()));
    }
}
