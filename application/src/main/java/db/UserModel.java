package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class UserModel extends Model {

  // defind table
  private static final String createTable = "" +
      "CREATE TABLE IF NOT EXISTS Users (" +
      "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
      "username VARCHAR(255) NOT NULL UNIQUE, " +
      "password VARCHAR(255) NOT NULL," +
      "email VARCHAR(255) NOT NULL," +
      "avatar VARCHAR(255) NOT NULL," +
      "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
      "deleted_at TIMESTAMP NULL DEFAULT NULL)";
  private final String tableName = "Users";
  private final String idName = "user_id";

  protected static String getCreateTable() {
    return createTable;
  }

  @Override
  protected String getTableName() {
    return tableName;
  }

  @Override
  protected String getIdName() {
    return idName;
  }

  // for model
  private int userId;
  private String username;
  private String password;
  private String email;
  private String avatar;
  private LinkedList<SongModel> likedSongs;

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

  public UserModel(int userId, String username) {
    this.userId = userId;
    this.username = new String(username);
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

  public static String hashPassword(String password) {
    return password;
  }

  public static UserModel match(String username, String password) {
    // call mysql to find user match username and password
    ConnectDB connectDB = new ConnectDB();

    try {
      Statement stmt = connectDB.getConnect().createStatement();
      String sql = "SELECT * FROM Users WHERE username = '" + username + "' AND password = '" + hashPassword(password)
          + "'";

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
    return "INSERT INTO " + getTableName() + " (username, password, email, avatar) VALUES (?, ?, ?, ?)";
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
      pstmt.setString(4, avatar);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected boolean checkAccess() {
    // check with username, email
    try {
      QueryResult qr1 = super.query("SELECT * FROM " + getTableName() + " WHERE username = ?", (pstmt) -> {
        try {
          pstmt.setString(1, username);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });

      if (qr1.getResultSet().next() == false) {
        qr1.close();

        try {
          QueryResult qr2 = super.query("SELECT * FROM " + getTableName() + " WHERE email = ?", (pstmt) -> {
            try {
              pstmt.setString(1, email);
            } catch (SQLException e) {
              e.printStackTrace();
            }
          });

          if (qr2.getResultSet().next() == false) {
            qr2.close();
            return true;
          }

          qr2.close();
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }

        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return false;
  }

  public boolean findData() {
    if (userId == -1) {
      return false;
    }

    try {
      QueryResult qr = super.findById();
      ResultSet rs = qr.getResultSet();
      if (rs.next()) {
        this.username = rs.getString("username");
        this.password = rs.getString("password");
        this.email = rs.getString("email");
        this.avatar = rs.getString("avatar");

        qr.close();
        return true;
      }

      qr.close();
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

  public LinkedList<SongModel> getLiked() {
    if (likedSongs == null) {
      likedSongs = new LinkedList<>();
      getLikedSongs();
    }

    return likedSongs;
  }

  public void getLikedSongs() {
    // get all liked songs
    UserLikes userLikes = new UserLikes(userId);
    QueryResult qr = userLikes.findByUserId();
    ResultSet rs = qr.getResultSet();

    try {
      while (rs.next()) {
        SongModel song = new SongModel(rs.getInt("song_id"));
        song.findData();

        likedSongs.add(song);

        System.out.println(song.getTitle() + " - " + userId);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    qr.close();
  }

  public boolean checkLikedSong(int songId) {
    UserLikes userLikes = new UserLikes(userId, songId);

    return userLikes.checkLikeUser();
  }

  public void likeSong(int songId) {
    UserLikes userLikes = new UserLikes(userId, songId);

    userLikes.insert();
  }

  public void unlikeSong(int songId) {
    UserLikes userLikes = new UserLikes(userId, songId);
    userLikes.findData();

    userLikes.delete();
  }

  public boolean updateUsername(String username) {
    this.username = username;

    return super.update("UPDATE " + getTableName() + " SET username = ? WHERE " + getIdName() + " = ?", (pstmt) -> {
      try {
        pstmt.setString(1, username);
        pstmt.setInt(2, userId);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

}
