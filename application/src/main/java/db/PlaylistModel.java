package db;

import java.sql.PreparedStatement;

public class PlaylistModel extends Model {
  private int playlistId;
  private String name;
  private int userId;

  public PlaylistModel(String name, int userId) {
    this.name = name;
    this.userId = userId;
  }

  public PlaylistModel(PlaylistModel playlist) {
    this.name = playlist.name;
    this.userId = playlist.userId;
  }

  @Override
  protected String getSQLString() {
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
  protected void setValues(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, name);
      pstmt.setInt(2, userId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
