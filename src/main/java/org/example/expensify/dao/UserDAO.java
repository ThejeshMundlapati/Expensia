package org.example.expensify.dao;

import java.sql.*;
import org.example.expensify.config.DB;
import org.example.expensify.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

  public User findByUsername(String username) throws Exception {
    String sql = "SELECT * FROM users WHERE username = ?";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setString(1, username);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          User u = new User();
          u.setUserId(rs.getInt("user_id"));
          u.setUsername(rs.getString("username"));
          u.setPasswordHash(rs.getString("password_hash"));
          u.setName(rs.getString("name"));
          u.setEmail(rs.getString("email"));
          u.setPhone(rs.getString("phone"));
          return u;
        }
      }
    }
    return null;
  }

  public User findById(int id) throws Exception {
    String sql = "SELECT * FROM users WHERE user_id = ?";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          User u = new User();
          u.setUserId(rs.getInt("user_id"));
          u.setUsername(rs.getString("username"));
          u.setPasswordHash(rs.getString("password_hash"));
          u.setName(rs.getString("name"));
          u.setEmail(rs.getString("email"));
          u.setPhone(rs.getString("phone"));
          return u;
        }
      }
    }
    return null;
  }

  public User create(String username, String rawPassword, String name, String email, String phone) throws Exception {
    String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    String sql = "INSERT INTO users(username, password_hash, name, email, phone) VALUES (?,?,?,?,?)";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, username);
      ps.setString(2, hash);
      ps.setString(3, name);
      ps.setString(4, email);
      ps.setString(5, phone);
      ps.executeUpdate();

      try (ResultSet keys = ps.getGeneratedKeys()) {
        if (keys.next()) {
          int id = keys.getInt(1);

          // Create default preferences
          try (PreparedStatement ps2 = c.prepareStatement(
              "INSERT INTO preferences(user_id) VALUES (?)")) {
            ps2.setInt(1, id);
            ps2.executeUpdate();
          }

          // Create default budget settings
          try (PreparedStatement ps3 = c.prepareStatement(
              "INSERT INTO budget_settings(user_id) VALUES (?)")) {
            ps3.setInt(1, id);
            ps3.executeUpdate();
          }

          return findById(id);
        }
      }
    }
    return null;
  }

  public boolean checkPassword(String username, String rawPassword) throws Exception {
    User u = findByUsername(username);
    if (u == null) return false;
    return BCrypt.checkpw(rawPassword, u.getPasswordHash());
  }
}