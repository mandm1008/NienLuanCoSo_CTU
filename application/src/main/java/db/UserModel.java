package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserModel extends Model {
  private int userId;
  private String username;
  private String password;
  private String email;

  public UserModel(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public UserModel(UserModel user) {
    this.username = user.username;
    this.password = user.password;
    this.email = user.email;
  }

  public static UserModel match(String username, String password) {
    // call mysql to find user match username and password
    ConnectDB connectDB = new ConnectDB();
    try {
      Statement stmt = connectDB.getConnect().createStatement();
      String sql = "SELECT * FROM Users WHERE username = '" + username + "' AND password = '" + password + "'";

      ResultSet result = stmt.executeQuery(sql);

      // check if user exists
      if (result.next()) {
        UserModel user = new UserModel(result.getString("username"), result.getString("password"),
            result.getString("email"));
        user.userId = result.getInt("user_id");
        connectDB.closeConnect();
        return user;
      } else {
        connectDB.closeConnect();
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      connectDB.closeConnect();
      return null;
    }
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
  public int getId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
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
