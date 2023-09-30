package com.fetch.takehometest.model;

import java.util.Comparator;

/**
 * Costum comparator for priority queue sorting by timestamp
 * 
 * @author Charlotte Wan
 */
public class TransactionComparator implements Comparator<TransactionRequest> {
    @Override
    public int compare(TransactionRequest t1, TransactionRequest t2) {
        return t1.getTimestamp().compareTo(t2.getTimestamp());
    }
}
