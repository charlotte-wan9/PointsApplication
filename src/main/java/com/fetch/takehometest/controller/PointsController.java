package com.fetch.takehometest.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fetch.takehometest.model.SpendResponse;
import com.fetch.takehometest.model.SpendRequest;
import com.fetch.takehometest.model.TransactionRequest;
import com.fetch.takehometest.payload.BalancePayload;
import com.fetch.takehometest.payload.Payload;
import com.fetch.takehometest.payload.SpendFailPayload;
import com.fetch.takehometest.payload.SpendSuccessPayload;
import com.fetch.takehometest.service.PointsService;

/**
 * Controller for PointsApplication
 * 
 * @author Charlotte Wan
 */
@RestController
public class PointsController {

    @Autowired
    private PointsService pointsService;

    /**
     * Prints a welcome message on the homepage
     * 
     * @return welcome message
     */
    @GetMapping("/")
    public String welcome() {
        return "<h1>Welcome to the Points Application!</h1>";
    }

    /**
     * Handles a post request to endpoint "/add"
     * 
     * @param transaction the request posted (a TransactionRequest)
     * @return a status code of 200 with no response body
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addPoints(@RequestBody TransactionRequest transaction) {
        // add transaction from request body to `transactions`
        pointsService.addPoints(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Handles a post request to endpoint "/spend"
     *  
     * @param spendRequest request postes (a SpendRequest)
     * @return a status code of 400 if the user doesn't have enough points
     *         a status code of 200 with a response body of "payer, points" pairs of points spent, otherwise
     */
    @PostMapping("/spend")
    public ResponseEntity<Payload> spendPoints(@RequestBody SpendRequest spendRequest) {
        int pointsToSpend = spendRequest.getPoints();
        
        // case where user does not have enough points
        if (pointsToSpend > pointsService.getTotal()) {
            SpendFailPayload payload = new SpendFailPayload();
            payload.setErrorMessage("User does not have enough points.");
            return new ResponseEntity<>(payload, HttpStatus.BAD_REQUEST);
        }

        ArrayList<SpendResponse> spendList = pointsService.spendPoints(pointsToSpend);
        SpendSuccessPayload payload = new SpendSuccessPayload();
        payload.setSpendList(spendList);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    /**
     * Handles a get request to endpoint "/balance"
     * 
     * @return a status code of 200 with a response body of user's balance for each payer
     */
    @GetMapping("/balance")
    public ResponseEntity<Payload> getPointsBalance() {
        HashMap<String, Integer> balance = pointsService.getBalance();
        BalancePayload payload = new BalancePayload();
        payload.setBalance(balance);
        return new ResponseEntity<>(payload, HttpStatus.OK);
    }
}
