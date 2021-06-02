package com.taylorstieff.heavenhr.exception;

public class ApplicationNotFoundException extends Exception {
    private long missingId;

    public ApplicationNotFoundException(long missingId) {
        super("Application of ID " + missingId + " does not exist");

        this.missingId = missingId;
    }

    public long getMissingId() {
        return missingId;
    }
}
