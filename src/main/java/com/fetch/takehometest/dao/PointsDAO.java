package com.fetch.takehometest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fetch.takehometest.model.TransactionRequest;

/**
 * DAO layer for PointsApplication
 * 
 * @author Charlotte Wan
 */
public interface PointsDAO extends JpaRepository<TransactionRequest, String>{
    
}