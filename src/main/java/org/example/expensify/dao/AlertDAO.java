package org.example.expensify.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.expensify.config.DB;
import org.example.expensify.model.Alert;

public class AlertDAO {
  public List<Alert> list(int userId, boolean onlyUnseen) throws Exception {
    String sql =
        "SELECT alert_id, user_id, alert_type, message, month, year, seen " +
            "FROM alerts " +
            "WHERE user_id=? " +
            (onlyUnseen ? "AND seen=0 " : "") +
            "ORDER BY created_at DESC";

    List<Alert> out = new ArrayList<>();
    try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Alert a = new Alert();
          a.setAlertId(rs.getInt(1));
          a.setUserId(rs.getInt(2));
          a.setAlertType(rs.getString(3));
          a.setMessage(rs.getString(4));
          a.setMonth(rs.getInt(5));
          a.setYear(rs.getInt(6));
          a.setSeen(rs.getBoolean(7));
          out.add(a);
        }
      }
    }
    return out;
  }

  public void markSeen(int userId, int alertId) throws Exception {
    String sql = "UPDATE alerts SET seen=1 WHERE alert_id=? AND user_id=?";
    try (Connection c = DB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, alertId);
      ps.setInt(2, userId);
      ps.executeUpdate();
    }
  }


  public int countUnseen(int userId) throws Exception {
    String sql = "SELECT COUNT(*) FROM alerts WHERE user_id = ? AND seen = 0";

    try (Connection c = DB.get();
         PreparedStatement ps = c.prepareStatement(sql)) {

      ps.setInt(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        }
      }
    }
    return 0;
  }
}
