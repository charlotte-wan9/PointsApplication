package com.fetch.takehometest.payload;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Representation of a response payload to endpoint "/balance"
 * 
 * @author Charlotte Wan
 */
public class BalancePayload implements Payload {
    @JsonValue
    HashMap<String, Integer> balance = new HashMap<>();

    public HashMap<String, Integer> getBalance() {
        return balance;
    }

    public void setBalance(HashMap<String, Integer> balance) {
        this.balance = balance;
    }
}
