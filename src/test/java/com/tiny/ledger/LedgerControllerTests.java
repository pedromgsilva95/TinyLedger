package com.tiny.ledger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.ledger.controller.LedgerController;
import com.tiny.ledger.domain.Transaction;
import com.tiny.ledger.domain.enums.TransactionType;
import com.tiny.ledger.service.LedgerService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class LedgerControllerTests {

  private static final double DEPOSIT_AMOUNT = 100.0;
  private static final double WITHDRAWAL_AMOUNT = 50.0;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private LedgerService ledgerService;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    // Ensure a clean state before each test (reset balance and transactions)
    ledgerService = new LedgerService();
    mockMvc = MockMvcBuilders.standaloneSetup(new LedgerController(ledgerService)).build();
  }

  @Test
  void testAddTransaction() throws Exception {
    // Prepare the request data
    TransactionType type = TransactionType.DEPOSIT;

    // Perform the POST request to add a transaction
    mockMvc.perform(post("/api/ledger/transaction")
            .param("amount", String.valueOf(DEPOSIT_AMOUNT))
            .param("type", type.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(result -> assertEquals("Transaction added successfully.", result.getResponse().getContentAsString()));
  }

  @Test
  void testGetBalance() throws Exception {
    // Prepare the request data: Add a deposit first
    ledgerService.addTransaction(DEPOSIT_AMOUNT, TransactionType.DEPOSIT);

    // Perform GET request to fetch the current balance
    MvcResult result = mockMvc.perform(get("/api/ledger/balance"))
        .andExpect(status().isOk())
        .andReturn();

    // Extract the balance from the response and assert it
    double balance = Double.parseDouble(result.getResponse().getContentAsString());
    assertEquals(DEPOSIT_AMOUNT, balance, "Balance should be 100 after deposit.");
  }

  @Test
  void testGetTransactionsHistory() throws Exception {
    // Add some transactions
    ledgerService.addTransaction(DEPOSIT_AMOUNT, TransactionType.DEPOSIT);
    ledgerService.addTransaction(WITHDRAWAL_AMOUNT, TransactionType.WITHDRAWAL);

    // Perform GET request to fetch the transaction history
    MvcResult result = mockMvc.perform(get("/api/ledger/transactionsHistory"))
        .andExpect(status().isOk())
        .andReturn();

    // Convert response to List of Transaction objects
    List<Transaction> transactions = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);

    // Assert the size of the transactions list
    assertEquals(2, transactions.size(), "There should be 2 transactions in the history.");
  }

}

