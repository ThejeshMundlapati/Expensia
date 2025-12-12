package org.example.expensify.dao;

import java.sql.*;
import org.example.expensify.config.DB;
import org.example.expensify.model.Preference;

public class PreferenceDAO {

  public Preference get(int userId) throws Exception {
    String sql = "SELECT * FROM preferences WHERE user_id = ?";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Preference p = new Preference();
          p.setPreferenceId(rs.getInt("preference_id"));
          p.setUserId(rs.getInt("user_id"));
          p.setThemeMode(rs.getString("theme_mode"));
          p.setCurrency(rs.getString("currency"));
          return p;
        }
      }
    }
    return null;
  }

  public void create(int userId) throws Exception {
    String sql = "INSERT INTO preferences (user_id) VALUES (?)";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.executeUpdate();
    }
  }

  public void update(int userId, String theme, String currency) throws Exception {
    String sql = "UPDATE preferences SET theme_mode = ?, currency = ? WHERE user_id = ?";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, theme);
      ps.setString(2, currency);
      ps.setInt(3, userId);
      ps.executeUpdate();
    }
  }
}