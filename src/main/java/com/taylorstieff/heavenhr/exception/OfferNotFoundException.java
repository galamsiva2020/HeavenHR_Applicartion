package com.taylorstieff.heavenhr.exception;

public class OfferNotFoundException extends Exception {
    private long missingId;

    public OfferNotFoundException(long missingId) {
        super("Offer of ID " + missingId + " does not exist");

        this.missingId = missingId;
    }

    public long getMissingId() {
        return missingId;
    }
}
