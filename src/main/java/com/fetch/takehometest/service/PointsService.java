package com.fetch.takehometest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    // hashmap to store the user's balance for each payer
    private final HashMap<String, Integer> balance = new HashMap<>();
    
    @Autowired
    private PointsDAO pointsDAO;

    /**
     * Adds a TransactionRequest to database
     * 
     * @param transaction TransactionRequest to add
     */
    public void addPoints(TransactionRequest transaction) {
        pointsDAO.save(transaction);

        // add `payer: points` pair to `balance`
        String payer = transaction.getPayer();
        Integer points = transaction.getPoints();
        if (!balance.containsKey(payer)) {
            balance.put(payer, points);
        } else {
            balance.put(payer, balance.get(payer) + points);
        }
    }

    /**
     * Gets `transactions` from DAO layer and updates accordingly based on the points spent
     *  
     * @param pointsToSpend points to spend
     * @return a list of SpendResponse
     */
    public ArrayList<SpendResponse> spendPoints(int pointsToSpend) {
        ArrayList<SpendResponse> spendList = new ArrayList<>();
        List<TransactionRequest> transactions = pointsDAO.findAll(Sort.by(Sort.Direction.ASC, "timestamp"));

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
                pointsDAO.delete(tr); // remove current `transaction` from database
            } else {
                updateSpendList(spendList, payer, -pointsToSpend);
                tr.setPoints(points - pointsToSpend); // update `transactions`
                balance.put(payer, balance.get(payer) - pointsToSpend); // update `balance`
                pointsDAO.save(tr); // update current `transaction` in database
                pointsToSpend = 0; // clear `pointsToSpend` to 0
            }
        }
        return spendList;
    }

    public HashMap<String, Integer> getBalance() {
        return balance;
    }

    /**
     * Helper method to calculate the user's total balance
     * 
     * @return user's total balance
     */
    public int getTotal() {
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
