package org.example.expensify.dao;

import java.sql.*;
import org.example.expensify.config.DB;
import org.example.expensify.model.BudgetSettings;

public class BudgetSettingsDAO {

  public BudgetSettings get(int userId) throws Exception {
    String sql = "SELECT * FROM budget_settings WHERE user_id = ?";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          BudgetSettings bs = new BudgetSettings();
          bs.setSettingId(rs.getInt("setting_id"));
          bs.setUserId(rs.getInt("user_id"));
          bs.setDefaultBudget(rs.getDouble("default_budget"));
          bs.setAlertAtFifty(rs.getBoolean("alert_at_fifty"));
          bs.setAlertAtNinety(rs.getBoolean("alert_at_ninety"));
          bs.setFiftyThreshold(rs.getInt("fifty_threshold"));
          return bs;
        }
      }
    }
    return null;
  }

  public void create(int userId) throws Exception {
    String sql = "INSERT INTO budget_settings (user_id) VALUES (?)";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.executeUpdate();
    }
  }

  public void update(int userId, double defaultBudget, boolean alertAtFifty,
                     boolean alertAtNinety, int fiftyThreshold) throws Exception {
    String sql = """
        UPDATE budget_settings
           SET default_budget = ?,
               alert_at_fifty = ?,
               alert_at_ninety = ?,
               fifty_threshold = ?
         WHERE user_id = ?
        """;

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setBigDecimal(1, java.math.BigDecimal.valueOf(defaultBudget));
      ps.setBoolean(2, alertAtFifty);
      ps.setBoolean(3, alertAtNinety);
      ps.setInt(4, fiftyThreshold);
      ps.setInt(5, userId);
      ps.executeUpdate();
    }
  }
}