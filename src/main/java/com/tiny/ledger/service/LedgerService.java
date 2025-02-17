package com.tiny.ledger.service;

import com.tiny.ledger.domain.Transaction;
import com.tiny.ledger.domain.enums.TransactionType;
import com.tiny.ledger.exception.InsufficientBalanceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Service;


@Service
public class LedgerService {

  @Getter
  private double balance = 0.0;
  private final List<Transaction> transactions = new ArrayList<>();

  public void addTransaction(double amount, TransactionType type) {

    validateWithdrawal(amount, type);

    if (type == TransactionType.DEPOSIT) {
      balance += amount;
    } else if (type == TransactionType.WITHDRAWAL) {
      balance -= amount;
    }

    Transaction transaction = new Transaction(amount, type, LocalDateTime.now());
    transactions.add(transaction);
  }

  private void validateWithdrawal(double amount, TransactionType type) {
    if (type == TransactionType.WITHDRAWAL && balance < amount) {
      throw new InsufficientBalanceException("Insufficient balance for this withdrawal.");
    }
  }

  public List<Transaction> getTransactionHistory() {
    return transactions;
  }

}