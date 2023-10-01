package com.fetch.takehometest.model;

/**
 * Object representation of spend request to endpoint "/spend"
 * 
 * @author Charlotte Wan
 */
public class SpendRequest {
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
