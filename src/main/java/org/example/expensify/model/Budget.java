package org.example.expensify.model;

public class Budget {
  private int budgetId;
  private int userId;
  private int month;
  private int year;
  private double budgetLimit;

  public int getBudgetId() {
    return budgetId;
  }

  public void setBudgetId(int budgetId) {
    this.budgetId = budgetId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public double getBudgetLimit() {
    return budgetLimit;
  }

  public void setBudgetLimit(double budgetLimit) {
    this.budgetLimit = budgetLimit;
  }}

