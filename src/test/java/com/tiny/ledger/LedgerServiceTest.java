package com.tiny.ledger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tiny.ledger.domain.Transaction;
import com.tiny.ledger.domain.enums.TransactionType;
import com.tiny.ledger.exception.InsufficientBalanceException;
import com.tiny.ledger.service.LedgerService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LedgerServiceTest {

  private static final double INITIAL_DEPOSIT_AMOUNT = 100.0;
  private static final double SECOND_DEPOSIT_AMOUNT = 200.0;
  private static final double WITHDRAWAL_AMOUNT = 50.0;
  private static final double EXPECTED_BALANCE_AFTER_WITHDRAWAL = 150.0;
  private static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance for this withdrawal.";

  private LedgerService ledgerService;

  @BeforeEach
  public void setUp() {
    ledgerService = new LedgerService();
  }

  @Test
  public void testDepositIncreasesBalance() {
    // Given
    // When
    ledgerService.addTransaction(INITIAL_DEPOSIT_AMOUNT, TransactionType.DEPOSIT);

    // Then
    assertEquals(INITIAL_DEPOSIT_AMOUNT, ledgerService.getBalance(), "Balance should be increased after deposit.");
  }

  @Test
  public void testWithdrawalDecreasesBalance() {
    // Given
    // Deposit some money first
    ledgerService.addTransaction(SECOND_DEPOSIT_AMOUNT, TransactionType.DEPOSIT);

    // When
    ledgerService.addTransaction(WITHDRAWAL_AMOUNT, TransactionType.WITHDRAWAL);

    // Assert
    assertEquals(EXPECTED_BALANCE_AFTER_WITHDRAWAL, ledgerService.getBalance(), "Balance should be decreased after withdrawal.");
  }

  @Test
  public void testWithdrawalThrowsExceptionWhenInsufficientBalance() {
    // Given
    // Then
    InsufficientBalanceException exception = assertThrows(
        InsufficientBalanceException.class,
        () -> ledgerService.addTransaction(INITIAL_DEPOSIT_AMOUNT, TransactionType.WITHDRAWAL),
        "Expected InsufficientBalanceException to be thrown"
    );

    assertEquals(INSUFFICIENT_BALANCE_MESSAGE, exception.getMessage());
  }

  @Test
  public void testTransactionHistory() {
    // Given
    // When
    ledgerService.addTransaction(INITIAL_DEPOSIT_AMOUNT, TransactionType.DEPOSIT);
    ledgerService.addTransaction(WITHDRAWAL_AMOUNT, TransactionType.WITHDRAWAL);

    // Then
    List<Transaction> transactions = ledgerService.getTransactionHistory();
    assertEquals(2, transactions.size(), "There should be 2 transactions in history.");
    assertEquals(TransactionType.DEPOSIT, transactions.get(0).getType(), "The first transaction should be a deposit.");
    assertEquals(TransactionType.WITHDRAWAL, transactions.get(1).getType(), "The second transaction should be a withdrawal.");
  }
}

