package com.fetch.takehometest.payload;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Representation of an unsuccessful response payload to endpoint "/spend"
 * 
 * @author Charlotte Wan
 */
public class SpendFailPayload implements Payload {
    @JsonValue
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
