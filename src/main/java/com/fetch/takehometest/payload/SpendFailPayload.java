package com.fetch.takehometest.payload;

import com.fasterxml.jackson.annotation.JsonValue;

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
