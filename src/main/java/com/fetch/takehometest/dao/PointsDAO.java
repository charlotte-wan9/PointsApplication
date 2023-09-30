package com.fetch.takehometest.dao;

import java.util.HashMap;
import java.util.PriorityQueue;

import org.springframework.stereotype.Repository;

import com.fetch.takehometest.model.TransactionComparator;
import com.fetch.takehometest.model.TransactionRequest;

/**
 * DAO layer for PointsApplication
 * 
 * @author Charlotte Wan
 */
@Repository
public class PointsDAO {
    // priority queue to store the transaction requests
    private final PriorityQueue<TransactionRequest> transactions =
        new PriorityQueue<>(new TransactionComparator());
    
    // hashmap to store the user's balance for each payer
    private final HashMap<String, Integer> balance = new HashMap<>();

    /**
     * Adds a TransactionRequest to `transactions`
     * 
     * @param transaction TransactionRequest to add
     */
    public void addPoints(TransactionRequest transaction) {
        transactions.add(transaction);

        // add `payer: points` pair to `balance`
        String payer = transaction.getPayer();
        Integer points = transaction.getPoints();
        if (!balance.containsKey(payer)) {
            balance.put(payer, points);
        } else {
            balance.put(payer, balance.get(payer) + points);
        }
    }

    public PriorityQueue<TransactionRequest> getTransactions() {
        return transactions;
    }

    public HashMap<String, Integer> getBalance() {
        return balance;
    }
}
