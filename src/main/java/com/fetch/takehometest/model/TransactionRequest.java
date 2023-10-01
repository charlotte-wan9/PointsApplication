package com.fetch.takehometest.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Object representation of TransactionRequest
 * 
 * @author Charlotte Wan
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @Id
    @GeneratedValue
    private int id;
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
