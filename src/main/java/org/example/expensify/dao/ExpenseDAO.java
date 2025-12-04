package org.example.expensify.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.expensify.config.DB;

public class ExpenseDAO {

  /**
   * Add expense via stored procedure (with currency)
   */
  public void addViaProcedure(int userId, int categoryId, double amount,
                              LocalDate date, String desc, String method,
                              String currency) throws Exception {

    final String call = "{CALL add_expense(?,?,?,?,?,?,?)}";

    try (Connection c = DB.get();
         CallableStatement cs = c.prepareCall(call)) {

      cs.setInt(1, userId);
      cs.setInt(2, categoryId);
      cs.setBigDecimal(3, java.math.BigDecimal.valueOf(amount));
      cs.setDate(4, Date.valueOf(date));

      if (desc == null || desc.trim().isEmpty()) {
        cs.setNull(5, Types.VARCHAR);
      } else {
        cs.setString(5, desc);
      }

      cs.setString(6, method);
      cs.setString(7, currency != null ? currency : "USD");
      cs.execute();
    }
  }

  /**
   * Convenience overload - defaults to USD currency
   */
  public void addViaProcedure(int userId, int categoryId, double amount,
                              LocalDate date, String desc, String method)
      throws Exception {
    addViaProcedure(userId, categoryId, amount, date, desc, method, "USD");
  }

  /**
   * Find a single expense by ID (with ownership check)
   */
  public Map<String, Object> findById(int userId, int expenseId) throws Exception {
    String sql = """
            SELECT e.expense_id, e.user_id, e.category_id,
                   e.amount, e.expense_date, e.description,
                   e.payment_method, e.currency,
                   c.category_name
            FROM expenses e
            JOIN categories c ON e.category_id = c.category_id
            WHERE e.user_id = ? AND e.expense_id = ?
        """;

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, expenseId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Map<String, Object> row = new HashMap<>();
          row.put("expenseId", rs.getInt("expense_id"));
          row.put("userId", rs.getInt("user_id"));
          row.put("categoryId", rs.getInt("category_id"));
          row.put("amount", rs.getDouble("amount"));
          row.put("expenseDate", rs.getDate("expense_date").toLocalDate());
          row.put("description", rs.getString("description"));
          row.put("paymentMethod", rs.getString("payment_method"));
          row.put("currency", rs.getString("currency"));
          row.put("categoryName", rs.getString("category_name"));
          return row;
        }
      }
    }
    return null;
  }

  /**
   * Update an existing expense
   */
  public void updateExpense(int userId, int expenseId, int categoryId,
                            double amount, LocalDate date, String desc,
                            String method) throws Exception {

    String sql = """
            UPDATE expenses
               SET category_id    = ?,
                   amount         = ?,
                   expense_date   = ?,
                   description    = ?,
                   payment_method = ?
             WHERE expense_id = ? AND user_id = ?
        """;

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, categoryId);
      ps.setBigDecimal(2, java.math.BigDecimal.valueOf(amount));
      ps.setDate(3, Date.valueOf(date));

      if (desc == null || desc.trim().isEmpty()) {
        ps.setNull(4, Types.VARCHAR);
      } else {
        ps.setString(4, desc);
      }

      ps.setString(5, method);
      ps.setInt(6, expenseId);
      ps.setInt(7, userId);

      ps.executeUpdate();
    }
  }

  /**
   * Get total spending for a specific month/year
   */
  public double monthTotal(int userId, int month, int year) throws Exception {
    String sql = """
            SELECT COALESCE(SUM(amount), 0.0)
            FROM expenses
            WHERE user_id = ?
              AND MONTH(expense_date) = ?
              AND YEAR(expense_date)  = ?
        """;

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, month);
      ps.setInt(3, year);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getDouble(1);
        }
      }
    }
    return 0.0;
  }

  /**
   * List expenses for a specific month/year
   */
  public List<Map<String, Object>> listByMonth(int userId, int month, int year)
      throws Exception {

    String sql = """
            SELECT e.expense_id, e.amount, e.expense_date, e.description,
                   e.payment_method, c.category_name, e.currency
            FROM expenses e
            JOIN categories c ON e.category_id = c.category_id
            WHERE e.user_id = ?
              AND MONTH(e.expense_date) = ?
              AND YEAR(e.expense_date)  = ?
            ORDER BY e.expense_date DESC, e.expense_id DESC
        """;

    List<Map<String, Object>> out = new ArrayList<>();

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, month);
      ps.setInt(3, year);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Map<String, Object> row = new HashMap<>();
          row.put("expenseId", rs.getInt("expense_id"));
          row.put("amount", rs.getDouble("amount"));
          row.put("expenseDate", rs.getDate("expense_date").toLocalDate());
          row.put("description", rs.getString("description"));
          row.put("paymentMethod", rs.getString("payment_method"));
          row.put("categoryName", rs.getString("category_name"));
          row.put("currency", rs.getString("currency"));
          out.add(row);
        }
      }
    }
    return out;
  }

  /**
   * List most recent expenses (limited)
   */
  public List<Map<String, Object>> listRecent(int userId, int limit) throws Exception {
    String sql = """
            SELECT e.expense_id, e.amount, e.expense_date, e.description,
                   e.payment_method, c.category_name, e.currency
            FROM expenses e
            JOIN categories c ON e.category_id = c.category_id
            WHERE e.user_id = ?
            ORDER BY e.expense_date DESC, e.expense_id DESC
            LIMIT ?
        """;

    List<Map<String, Object>> out = new ArrayList<>();

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, limit);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Map<String, Object> row = new HashMap<>();
          row.put("expenseId", rs.getInt("expense_id"));
          row.put("expenseDate", rs.getDate("expense_date").toLocalDate());
          row.put("amount", rs.getDouble("amount"));
          row.put("description", rs.getString("description"));
          row.put("paymentMethod", rs.getString("payment_method"));
          row.put("categoryName", rs.getString("category_name"));
          row.put("currency", rs.getString("currency"));
          out.add(row);
        }
      }
    }
    return out;
  }

  /**
   * Delete an expense (with ownership check)
   */
  public void delete(int userId, int expenseId) throws Exception {
    String sql = "DELETE FROM expenses WHERE expense_id = ? AND user_id = ?";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, expenseId);
      ps.setInt(2, userId);
      ps.executeUpdate();
    }
  }

  /**
   * List all expenses for a user
   */
  public List<Map<String, Object>> listAll(int userId) throws Exception {
    String sql = """
            SELECT e.expense_id, e.amount, e.expense_date, e.description,
                   e.payment_method, c.category_name, e.currency
            FROM expenses e
            JOIN categories c ON e.category_id = c.category_id
            WHERE e.user_id = ?
            ORDER BY e.expense_date DESC, e.expense_id DESC
        """;

    List<Map<String, Object>> out = new ArrayList<>();

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Map<String, Object> row = new HashMap<>();
          row.put("expenseId", rs.getInt("expense_id"));
          row.put("amount", rs.getDouble("amount"));
          row.put("expenseDate", rs.getDate("expense_date").toLocalDate());
          row.put("description", rs.getString("description"));
          row.put("paymentMethod", rs.getString("payment_method"));
          row.put("categoryName", rs.getString("category_name"));
          row.put("currency", rs.getString("currency"));
          out.add(row);
        }
      }
    }
    return out;
  }

  /**
   * List expenses within a date range
   */
  public List<Map<String, Object>> listByDateRange(int userId, LocalDate from, LocalDate to)
      throws Exception {

    String sql;
    boolean singleDate = (to == null);

    if (singleDate) {
      sql = """
            SELECT e.expense_id, e.amount, e.expense_date, e.description,
                   e.payment_method, c.category_name, e.currency
            FROM expenses e
            JOIN categories c ON e.category_id = c.category_id
            WHERE e.user_id = ?
              AND e.expense_date = ?
            ORDER BY e.expense_date DESC
        """;
    } else {
      sql = """
            SELECT e.expense_id, e.amount, e.expense_date, e.description,
                   e.payment_method, c.category_name, e.currency
            FROM expenses e
            JOIN categories c ON e.category_id = c.category_id
            WHERE e.user_id = ?
              AND e.expense_date BETWEEN ? AND ?
            ORDER BY e.expense_date DESC
        """;
    }

    List<Map<String, Object>> out = new ArrayList<>();

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setDate(2, Date.valueOf(from));

      if (!singleDate) {
        ps.setDate(3, Date.valueOf(to));
      }

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Map<String, Object> row = new HashMap<>();
          row.put("expenseId", rs.getInt("expense_id"));
          row.put("expenseDate", rs.getDate("expense_date").toLocalDate());
          row.put("amount", rs.getDouble("amount"));
          row.put("description", rs.getString("description"));
          row.put("paymentMethod", rs.getString("payment_method"));
          row.put("categoryName", rs.getString("category_name"));
          row.put("currency", rs.getString("currency"));
          out.add(row);
        }
      }
    }
    return out;
  }
}