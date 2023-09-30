package com.fetch.takehometest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import org.springframework.stereotype.Service;

import com.fetch.takehometest.dao.PointsDAO;
import com.fetch.takehometest.model.SpendResponse;
import com.fetch.takehometest.model.TransactionRequest;

/**
 * Service layer for PointsApplication
 * 
 * @author Charlotte Wan
 */
@Service
public class PointsService {
    private final PointsDAO pointsDAO;

    public PointsService(PointsDAO pointsDAO) {
        this.pointsDAO = pointsDAO;
    }

    /**
     * Calls `addPoints` in DAO layer to add a TransactionRequest to `transactions`
     * 
     * @param transaction TransactionRequest to add
     */
    public void addPoints(TransactionRequest transaction) {
        pointsDAO.addPoints(new TransactionRequest(transaction.getPayer(),
                                                   transaction.getPoints(),
                                                   transaction.getTimestamp()));
    }

    /**
     * Gets `transactions` and `balance` from DAO layer and updates accordingly based on the points spent
     *  
     * @param pointsToSpend points to spend
     * @return a list of SpendResponse
     */
    public ArrayList<SpendResponse> spendPoints(int pointsToSpend) {
        ArrayList<SpendResponse> spendList = new ArrayList<>();
        PriorityQueue<TransactionRequest> transactions = pointsDAO.getTransactions();
        HashMap<String, Integer> balance = pointsDAO.getBalance();

        // travers `transactions`
        Iterator<TransactionRequest> iterator = transactions.iterator();
        while (pointsToSpend > 0 && iterator.hasNext()) {
            TransactionRequest tr = iterator.next();
            String payer = tr.getPayer();
            int points = tr.getPoints();

            if (points < pointsToSpend) {
                updateSpendList(spendList, payer, -points);
                pointsToSpend -= points; // decrement `pointsToSpend` by `points`
                balance.put(payer, balance.get(payer) - points); // update `balance`
                iterator.remove(); // remove current `transaction` from queue
            } else {
                updateSpendList(spendList, payer, -pointsToSpend);
                tr.setPoints(points - pointsToSpend); // update `transactions`
                balance.put(payer, balance.get(payer) - pointsToSpend); // update `balance`
                pointsToSpend = 0; // clear `pointsToSpend` to 0
            }
        }
        return spendList;
    }

    public HashMap<String, Integer> getBalance() {
        return pointsDAO.getBalance();
    }

    /**
     * Helper method to calculate the user's total balance
     * 
     * @return user's total balance
     */
    public int getTotal() {
        HashMap<String, Integer> balance = pointsDAO.getBalance();
        Integer total = 0;
        for (Integer val : balance.values()) {
            total += val;
        }
        return total;
    }

    /**
     * Private helper method to update existing `spendResponse` in `responseList` in `spendPoints`
     * instead of adding a new one every time
     * 
     * @param spendList list to update
     * @param payer     payer name to match
     * @param points    points to update
     */
    private void updateSpendList(ArrayList<SpendResponse> spendList, String payer, int points) {
        SpendResponse existingSpendResponse = null;
        for (SpendResponse response : spendList) {
            // if payer name already exist in `spendList`, set `existingSpendResponse`
            if (response.getPayer().equals(payer)) {
                existingSpendResponse = response;
            }
        }

        // update `existingSpendResponse` if there is one, else add a new spendResponse
        if (existingSpendResponse != null) {
            existingSpendResponse.setPoints(existingSpendResponse.getPoints() + points);
        } else {
            spendList.add(new SpendResponse(payer, points));
        }
    }
}
