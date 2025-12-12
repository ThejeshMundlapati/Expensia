package org.example.expensify.model;

public class BudgetSettings {
  private int settingId;
  private int userId;
  private double defaultBudget;
  private boolean alertAtFifty;
  private boolean alertAtNinety;
  private int fiftyThreshold;

  public int getSettingId() {
    return settingId;
  }

  public void setSettingId(int settingId) {
    this.settingId = settingId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public double getDefaultBudget() {
    return defaultBudget;
  }

  public void setDefaultBudget(double defaultBudget) {
    this.defaultBudget = defaultBudget;
  }

  public boolean isAlertAtFifty() {
    return alertAtFifty;
  }

  public void setAlertAtFifty(boolean alertAtFifty) {
    this.alertAtFifty = alertAtFifty;
  }

  public boolean isAlertAtNinety() {
    return alertAtNinety;
  }

  public void setAlertAtNinety(boolean alertAtNinety) {
    this.alertAtNinety = alertAtNinety;
  }

  public int getFiftyThreshold() {
    return fiftyThreshold;
  }

  public void setFiftyThreshold(int fiftyThreshold) {
    this.fiftyThreshold = fiftyThreshold;
  }
}