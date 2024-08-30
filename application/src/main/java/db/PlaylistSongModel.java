package db;

import java.sql.PreparedStatement;

public class PlaylistSongModel extends Model {
  private int playlistSongId;
  private int playlistId;
  private int songId;

  public PlaylistSongModel(int playlistId, int songId) {
    this.playlistId = playlistId;
    this.songId = songId;
  }

  public PlaylistSongModel(PlaylistSongModel playlistSong) {
    this.playlistId = playlistSong.playlistId;
    this.songId = playlistSong.songId;
  }

  @Override
  protected String getSQLString() {
    return "INSERT INTO PlaylistSongs (playlist_id, song_id) VALUES (?, ?)";
  }

  @Override
  protected String getTableName() {
    return "PlaylistSongs";
  }

  @Override
  protected int getId() {
    return playlistSongId;
  }

  @Override
  protected void setValues(PreparedStatement pstmt) {
    try {
      pstmt.setInt(1, playlistId);
      pstmt.setInt(2, songId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
