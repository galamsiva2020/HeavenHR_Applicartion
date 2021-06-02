package com.taylorstieff.heavenhr.exception;

public class InvalidApplicationStatusException extends Exception {
    private String badStatus;

    public InvalidApplicationStatusException(String badStatus) {
        super("Application status of " + badStatus + " does not exist");

        this.badStatus = badStatus;
    }

    public String getBadStatus() {
        return badStatus;
    }
}
