package org.example.expensify.model;


public class Preference {
  private int preferenceId;
  private int userId;
  private String themeMode; // LIGHT/DARK
  private String currency;  // USD, INR, ...
  private double defaultBudget;

  // getters/setters
  public int getPreferenceId() {
    return preferenceId;
  }

  public void setPreferenceId(int preferenceId) {
    this.preferenceId = preferenceId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getThemeMode() {
    return themeMode;
  }

  public void setThemeMode(String themeMode) {
    this.themeMode = themeMode;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public double getDefaultBudget() {
    return defaultBudget;
  }

  public void setDefaultBudget(double defaultBudget) {
    this.defaultBudget = defaultBudget;
  }}

