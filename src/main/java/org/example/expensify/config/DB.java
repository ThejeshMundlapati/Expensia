package org.example.expensify.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DB {
  private static String URL;
  private static String USER;
  private static String PASS;

  static {
    try {
      Properties props = new Properties();
      InputStream in = DB.class.getClassLoader().getResourceAsStream("db.properties");
      props.load(in);
      URL = props.getProperty("db.url");
      USER = props.getProperty("db.user");
      PASS = props.getProperty("db.password");
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (Exception e) {
      throw new RuntimeException("Failed to load DB config", e);
    }
  }

  public static Connection get() throws Exception {
    return DriverManager.getConnection(URL, USER, PASS);
  }
}
