package org.example.expensify.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.expensify.config.DB;
import org.example.expensify.model.Category;

public class CategoryDAO {

  public List<Category> listForUser(int userId) throws Exception {
    String sql = """
          SELECT category_id, category_name, is_default, user_id
          FROM categories
          WHERE is_default=1 OR user_id=?
          ORDER BY is_default DESC, category_name
          """;

    List<Category> out = new ArrayList<>();

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Category cat = new Category();
          cat.setCategoryId(rs.getInt(1));
          cat.setCategoryName(rs.getString(2));
          cat.setDefault(rs.getBoolean(3));

          int uid = rs.getInt(4);
          cat.setUserId(rs.wasNull() ? null : uid);

          out.add(cat);
        }
      }
    }
    return out;
  }


  public int createAndReturnId(int userId, String name) throws Exception {
    String sql = "INSERT INTO categories(category_name, is_default, user_id) VALUES (?,0,?)";
    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, name);
      ps.setInt(2, userId);
      ps.executeUpdate();

      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) return rs.getInt(1);
      }
    }
    throw new Exception("Could not create category");
  }


}
