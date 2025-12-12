package org.example.expensify.model;

public class Preference {
  private int preferenceId;
  private int userId;
  private String themeMode;
  private String currency;

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
}