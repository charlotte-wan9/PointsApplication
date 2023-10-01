package com.fetch.takehometest.payload;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fetch.takehometest.model.SpendResponse;

public class SpendSuccessPayload implements Payload {
    @JsonValue
    private ArrayList<SpendResponse> spendList = new ArrayList<>();

    public ArrayList<SpendResponse> getSpendList() {
        return spendList;
    }

    public void setSpendList(ArrayList<SpendResponse> spendList) {
        this.spendList = spendList;
    }
}
