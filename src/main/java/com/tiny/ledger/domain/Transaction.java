package com.tiny.ledger.domain;

import com.tiny.ledger.domain.enums.TransactionType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, toString, equals, hashCode
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
  private double amount;
  private TransactionType type; // "DEPOSIT" ou "WITHDRAWAL"
  private LocalDateTime timestamp;
}