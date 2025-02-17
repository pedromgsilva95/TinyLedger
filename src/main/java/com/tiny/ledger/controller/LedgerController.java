package com.tiny.ledger.controller;

import com.tiny.ledger.domain.Transaction;
import com.tiny.ledger.domain.enums.TransactionType;
import com.tiny.ledger.service.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {
  private final LedgerService ledgerService;

  @Autowired
  public LedgerController(LedgerService ledgerService) {
    this.ledgerService = ledgerService;
  }

  @Operation(summary = "Add a transaction", description = "Record a new transaction (deposit or withdrawal).")
  @PostMapping("/transaction")
  public ResponseEntity<String> addTransaction(@RequestParam double amount, @RequestParam @Valid TransactionType type) {
    ledgerService.addTransaction(amount, type);
    return ResponseEntity.ok("Transaction added successfully.");
  }

  @Operation(summary = "Get current balance", description = "Retrieve the current balance of the ledger.")
  @GetMapping("/balance")
  public ResponseEntity<Double> getBalance() {
    return ResponseEntity.ok(ledgerService.getBalance());
  }

  @Operation(summary = "Get transaction history", description = "Retrieve the full list of recorded transactions.")
  @GetMapping("/transactionsHistory")
  public ResponseEntity<List<Transaction>> getTransactions() {
    return ResponseEntity.ok(ledgerService.getTransactionHistory());
  }
}