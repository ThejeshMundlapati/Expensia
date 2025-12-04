package org.example.expensify.model;

public class User {
  private int userId;
  private String username;
  private String passwordHash;
  private String name;
  private String email;
  private String phone;

  public int getUserId() { return userId; }
  public void setUserId(int id) { this.userId = id; }
  public String getUsername() { return username; }
  public void setUsername(String u) { this.username = u; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String p) { this.passwordHash = p; }
  public String getName() { return name; }
  public void setName(String n) { this.name = n; }
  public String getEmail() { return email; }
  public void setEmail(String e) { this.email = e; }
  public String getPhone() { return phone; }
  public void setPhone(String p) { this.phone = p; }
}
