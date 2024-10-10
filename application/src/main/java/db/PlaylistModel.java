package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class PlaylistModel extends Model {

  // defind table
  private static final String createTable = "" +
      "CREATE TABLE IF NOT EXISTS Playlists (" +
      "playlist_id INT AUTO_INCREMENT PRIMARY KEY, " +
      "name VARCHAR(255) NOT NULL, " +
      "user_id INT, " +
      "FOREIGN KEY (user_id) REFERENCES Users(user_id))";
  private final String tableName = "Playlists";
  private final String idName = "playlist_id";

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
  private int playlistId;
  private String name;
  private int userId;

  public PlaylistModel() {
    this.playlistId = -1;
    this.name = "";
    this.userId = -1;
  }

  public PlaylistModel(int playlistId) {
    this.playlistId = playlistId;
  }

  public PlaylistModel(String name, int userId) {
    this.name = new String(name);
    this.userId = userId;
  }

  public PlaylistModel(PlaylistModel playlist) {
    this.name = new String(playlist.name);
    this.userId = playlist.userId;
  }

  @Override
  protected String getInsertString() {
    return "INSERT INTO " + getTableName() + " (name, user_id) VALUES (?, ?)";
  }

  @Override
  protected int getId() {
    return playlistId;
  }

  @Override
  protected void setValueInsert(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, name);
      pstmt.setInt(2, userId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected boolean checkAccess() {
    // check with title
    try {
      QueryResult qr = super.query("SELECT * FROM " + getTableName() + " WHERE (user_id, name) = (?, ?)", (pstmt) -> {
        try {
          pstmt.setInt(1, userId);
          pstmt.setString(2, name);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
      if (qr.getResultSet().next() == false) {
        qr.close();
        return true;
      }

      qr.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return false;
  }

  @Override
  public boolean delete() {
    if (playlistId == -1) {
      return false;
    }

    if (new PlaylistSongModel().deletePlaylist(playlistId)) {
      return super.delete();
    } else {
      return false;
    }
  }

  public boolean findData() {
    if (playlistId == -1) {
      return false;
    }

    try {
      QueryResult qr = super.findById();
      ResultSet rs = qr.getResultSet();
      if (rs.next()) {
        this.name = rs.getString("name");
        this.userId = rs.getInt("user_id");

        qr.close();
        return true;
      }

      qr.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  public String getName() {
    return name;
  }

  public int getUserId() {
    return userId;
  }

  public int getPlaylistId() {
    return playlistId;
  }

  public UserModel getUser() {
    UserModel user = new UserModel(userId);
    user.findData();

    return user;
  }

  public LinkedList<PlaylistModel> findByUserId(int userId) {
    LinkedList<PlaylistModel> playlists = new LinkedList<PlaylistModel>();

    try {
      QueryResult qr = super.query("SELECT * FROM " + getTableName() + " WHERE user_id = ?", (pstmt) -> {
        try {
          pstmt.setInt(1, userId);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
      ResultSet rs = qr.getResultSet();

      while (rs.next()) {
        PlaylistModel playlist = new PlaylistModel();
        playlist.playlistId = rs.getInt("playlist_id");
        playlist.name = rs.getString("name");
        playlist.userId = rs.getInt("user_id");

        playlists.add(playlist);
      }

      qr.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return playlists;
  }

}
