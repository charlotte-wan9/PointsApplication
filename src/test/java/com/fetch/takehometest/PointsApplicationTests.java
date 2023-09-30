package com.fetch.takehometest;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetch.takehometest.model.SpendRequest;
import com.fetch.takehometest.model.TransactionRequest;
import com.fetch.takehometest.service.PointsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit and integration tests for PointsController using JUnit and MockMvc
 * 
 * @author Charlotte Wan
 */
@SpringBootTest
@AutoConfigureMockMvc
class PointsApplicationTests {
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

	@Autowired
	private pointsService pointsService;

	/**
	 * After each test case, spend all balance so that requests to endpoint "/add" in individual cases
	 * doesn't affect one another
	 * 
	 * @throws Exception
	 */
	@AfterEach
	void spendAll() throws Exception {
		SpendRequest spendRequest = new SpendRequest();
		spendRequest.setPoints(pointsService.getTotal());

		mockMvc.perform(post("/spend")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(spendRequest)))
				.andExpect(status().isOk());
	}

	/** 
	 * addPoints() unit test
	 * @throws Exception
	 */
	@Test
	void testAddPoints() throws Exception {
		// Initialize a new TransactionRequest
        TransactionRequest tr1 = new TransactionRequest
			("DANNON", 300, Instant.parse("2022-10-31T10:00:00Z"));

		// mockMvc performs a post request to endpoint "/add", and check status code is 200 
        mockMvc.perform(post("/add")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(tr1)))
               .andExpect(status().isOk());
    }

	/** 
	 * spendPoints() unit test
	 * @throws Exception
	 */
	@Test
	void testSpendPoints() throws Exception {
		// post transaction requests
		addPoints("UNILEVER", 200, Instant.parse("2022-10-31T11:00:00Z"));
		addPoints("DANNON", 300, Instant.parse("2022-10-31T10:00:00Z"));
		
		// Initialize a new SpendRequest
		SpendRequest spendRequest = new SpendRequest();
		spendRequest.setPoints(500);

		// mockMvc performs a post request to endpoint "/spend", and check status code is 200
		// and response body is correct
		mockMvc.perform(post("/spend")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(spendRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].payer").value("DANNON"))
				.andExpect(jsonPath("$[0].points").value("-300"))
				.andExpect(jsonPath("$[1].payer").value("UNILEVER"))
				.andExpect(jsonPath("$[1].points").value("-200"));
	}

	/** 
	 * spendPoints() unit test - case where user does not have enough points
	 * @throws Exception
	 */
	@Test
	void testSpendPointsNotEnough() throws Exception {
		// post transaction requests
		addPoints("UNILEVER", 200, Instant.parse("2022-10-31T11:00:00Z"));
		addPoints("DANNON", 300, Instant.parse("2022-10-31T10:00:00Z"));
		
		// Initialize a new SpendRequest with more than user points
		SpendRequest spendRequest = new SpendRequest();
		spendRequest.setPoints(1000);

		// mockMvc performs a post request to endpoint "/spend", and check status code is 400
		// and response body is correct
		mockMvc.perform(post("/spend")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(spendRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("User does not have enough points."));
	}

	/** 
	 * getBalance() unit test
	 * @throws Exception
	 */
	@Test
	void testGetBalance() throws Exception {
		// post transaction requests
		addPoints("UNILEVER", 200, Instant.parse("2022-10-31T11:00:00Z"));
		addPoints("DANNON", 300, Instant.parse("2022-10-31T10:00:00Z"));
		addPoints("MILLER COORS", 10000, Instant.parse("2022-11-01T14:00:00Z"));

		// mockMvc performs a get request to endpoint "/balance", and check status code is 200
		// and response body is correct
		mockMvc.perform(get("/balance"))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.DANNON").value(300))
			   .andExpect(jsonPath("$.UNILEVER").value(200))
			   .andExpect(jsonPath("$.['MILLER COORS']").value(10000));
	}

	/** 
	 * PointsController integration test
	 * @throws Exception
	 */
	@Test
	void testAll() throws Exception {
		// post transaction requests
		addPoints("DANNON", 300, Instant.parse("2022-10-31T10:00:00Z"));
		addPoints("UNILEVER", 200, Instant.parse("2022-10-31T11:00:00Z"));
		addPoints("DANNON", -200, Instant.parse("2022-10-31T15:00:00Z"));
		addPoints("MILLER COORS", 10000, Instant.parse("2022-11-01T14:00:00Z"));
		addPoints("DANNON", 1000, Instant.parse("2022-11-02T14:00:00Z"));

		// Initialize a new SpendRequest
		SpendRequest spendRequest = new SpendRequest();
		spendRequest.setPoints(5000);

		// mockMvc performs a post request to endpoint "/spend", and check status code is 200
		// and response body is correct
		mockMvc.perform(post("/spend")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(spendRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].payer").value("DANNON"))
				.andExpect(jsonPath("$[0].points").value("-100"))
				.andExpect(jsonPath("$[1].payer").value("UNILEVER"))
				.andExpect(jsonPath("$[1].points").value("-200"))
				.andExpect(jsonPath("$[2].payer").value("MILLER COORS"))
				.andExpect(jsonPath("$[2].points").value("-4700"));

		// mockMvc performs a get request to endpoint "/balance", and check status code is 200
		// and response body is correct
		mockMvc.perform(get("/balance"))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.DANNON").value(1000))
			   .andExpect(jsonPath("$.UNILEVER").value(0))
			   .andExpect(jsonPath("$.['MILLER COORS']").value(5300));
	}

	/** 
	 * Private helper method in which mockMvc performs a post request to the endpoint "/add"
	 * 
	 * @param payer     payer of the transaction
	 * @param points    points to add for the transaction
	 * @param timestamp timestamp of the transaction
	 * @throws Exception
	 */
	private void addPoints(String payer, int points, Instant timestamp) throws Exception {
		TransactionRequest tr = new TransactionRequest(payer, points, timestamp);

		mockMvc.perform(post("/add")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(tr)))
               .andExpect(status().isOk());
	}
}
