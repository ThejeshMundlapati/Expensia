package org.example.expensify.model;

import java.time.LocalDate;

public class Expense {
  private int expenseId;
  private int userId;
  private int categoryId;
  private double amount;
  private LocalDate expenseDate;
  private String description;
  private String paymentMethod;

  private String currency;

  public int getExpenseId() { return expenseId; }
  public void setExpenseId(int expenseId) { this.expenseId = expenseId; }

  public int getUserId() { return userId; }
  public void setUserId(int userId) { this.userId = userId; }

  public int getCategoryId() { return categoryId; }
  public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

  public double getAmount() { return amount; }
  public void setAmount(double amount) { this.amount = amount; }

  public LocalDate getExpenseDate() { return expenseDate; }
  public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getPaymentMethod() { return paymentMethod; }
  public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
}
