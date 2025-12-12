package org.example.expensify.dao;

import java.sql.*;
import org.example.expensify.config.DB;
import org.example.expensify.model.Budget;

public class BudgetDAO {
  public Budget upsert(int userId, int month, int year, double limit) throws Exception {
    String sql = """
      INSERT INTO budgets(user_id, month, year, budget_limit)
      VALUES (?,?,?,?)
      ON DUPLICATE KEY UPDATE budget_limit=VALUES(budget_limit)
      """;
    try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.setInt(2, month);
      ps.setInt(3, year);
      ps.setBigDecimal(4, java.math.BigDecimal.valueOf(limit));
      ps.executeUpdate();
    }
    return find(userId, month, year);
  }

  public Budget find(int userId, int month, int year) throws Exception {
    String sql = "SELECT * FROM budgets WHERE user_id=? AND month=? AND year=?";
    try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, userId); ps.setInt(2, month); ps.setInt(3, year);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Budget b = new Budget();
          b.setBudgetId(rs.getInt("budget_id"));
          b.setUserId(rs.getInt("user_id"));
          b.setMonth(rs.getInt("month"));
          b.setYear(rs.getInt("year"));
          b.setBudgetLimit(rs.getDouble("budget_limit"));
          return b;
        }
      }
    }
    return null;
  }
}

