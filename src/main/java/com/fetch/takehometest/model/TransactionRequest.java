package com.fetch.takehometest.model;

import java.time.Instant;

/**
 * Object representation of a transaction request to endpoint "/add"
 * 
 * @author Charlotte Wan
 */
public class TransactionRequest {
    private String payer;
    private int points;
    private Instant timestamp;
    
    public TransactionRequest(String payer, int points, Instant timestamp) {
        this.payer = payer;
        this.points = points;
        this.timestamp = timestamp;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }    
}
