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
  private String avatar;

  public UserModel() {
    this.userId = -1;
    this.username = "";
    this.password = "";
    this.email = "";
    this.avatar = "";
  }

  public UserModel(int userId) {
    this.userId = userId;
  }

  public UserModel(String username, String password, String email) {
    this.username = new String(username);
    this.password = new String(password);
    this.email = new String(email);
    this.avatar = "";
  }

  public UserModel(String username, String password, String email, String avatar) {
    this.username = new String(username);
    this.password = new String(password);
    this.email = new String(email);
    this.avatar = new String(avatar);
  }

  public UserModel(UserModel user) {
    this.username = new String(user.username);
    this.password = new String(user.password);
    this.email = new String(user.email);
    this.avatar = new String(user.avatar);
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
            result.getString("email"), result.getString("avatar"));

        user.userId = result.getInt("user_id");

        return user;
      }

      return null;
    } catch (SQLException e) {
      e.printStackTrace();

      return null;
    } finally {
      connectDB.closeConnect();
    }
  }

  @Override
  protected String getInsertString() {
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

  @Override
  protected void setValueInsert(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, username);
      pstmt.setString(2, password);
      pstmt.setString(3, email);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean findData() {
    if (userId == -1) {
      return false;
    }

    try {
      ResultSet rs = super.findById();
      if (rs.next()) {
        this.username = rs.getString("username");
        this.password = rs.getString("password");
        this.email = rs.getString("email");
        this.avatar = rs.getString("avatar");

        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getAvatar() {
    return avatar;
  }

  public int getUserId() {
    return userId;
  }

}
