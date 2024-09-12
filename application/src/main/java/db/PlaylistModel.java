package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlaylistModel extends Model {
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
    return "INSERT INTO Playlists (name, user_id) VALUES (?, ?)";
  }

  @Override
  protected String getTableName() {
    return "Playlists";
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

  public boolean findData() {
    if (playlistId == -1) {
      return false;
    }

    try {
      ResultSet rs = super.findById();
      if (rs.next()) {
        this.name = rs.getString("name");
        this.userId = rs.getInt("user_id");
        return true;
      }
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

}
