package db;

import java.sql.PreparedStatement;

public class SongSchema extends Schema {
  private String title;
  private int duration;
  private int albumId;
  private int artistId;

  public SongSchema(String title, int duration, int albumId, int artistId) {
    this.title = title;
    this.duration = duration;
    this.albumId = albumId;
    this.artistId = artistId;
  }

  public SongSchema(SongSchema song) {
    this.title = song.title;
    this.duration = song.duration;
    this.albumId = song.albumId;
    this.artistId = song.artistId;
  }

  @Override
  protected String getSQLString() {
    return "INSERT INTO Songs (title, duration, album_id, artist_id) VALUES (?, ?, ?, ?)";
  }

  @Override
  protected String getTableName() {
    return "Songs";
  }

  @Override
  protected void setValues(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, title);
      pstmt.setInt(2, duration);
      pstmt.setInt(3, albumId);
      pstmt.setInt(4, artistId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
