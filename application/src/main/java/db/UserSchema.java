package db;

import java.sql.PreparedStatement;

public class UserSchema extends Schema {
  private int userId;
  private String username;
  private String password;
  private String email;

  public UserSchema(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public UserSchema(UserSchema user) {
    this.username = user.username;
    this.password = user.password;
    this.email = user.email;
  }

  @Override
  protected String getSQLString() {
    return "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";
  }

  @Override
  protected String getTableName() {
    return "Users";
  }

  @Override
  protected int getId() {
    return userId;
  }

  @Override
  protected void setValues(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, username);
      pstmt.setString(2, password);
      pstmt.setString(3, email);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
