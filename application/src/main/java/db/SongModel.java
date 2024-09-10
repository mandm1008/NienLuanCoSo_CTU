package db;

import java.sql.PreparedStatement;

public class SongModel extends Model {
  private int songId;
  private String title;
  private int duration;
  private int albumId;
  private int artistId;
  private String href;

  public SongModel() {
    this.songId = -1;
    this.title = "";
    this.duration = 0;
    this.albumId = -1;
    this.artistId = -1;
    this.href = "";
  }

  public SongModel(String title, int duration, int albumId, int artistId, String href) {
    this.title = title;
    this.duration = duration;
    this.albumId = albumId;
    this.artistId = artistId;
    this.href = href;
  }

  public SongModel(SongModel song) {
    this.title = song.title;
    this.duration = song.duration;
    this.albumId = song.albumId;
    this.artistId = song.artistId;
    this.href = song.href;
  }

  @Override
  protected String getSQLString() {
    return "INSERT INTO Songs (title, duration, album_id, artist_id, href) VALUES (?, ?, ?, ?, ?)";
  }

  @Override
  protected String getTableName() {
    return "Songs";
  }

  @Override
  protected int getId() {
    return songId;
  }

  public int getSongId() {
    return songId;
  }

  public String getTitle() {
    return title;
  }

  public int getDuration() {
    return duration;
  }

  public int getAlbumId() {
    return albumId;
  }

  public int getArtistId() {
    return artistId;
  }

  public String getHref() {
    return href;
  }

  @Override
  protected void setValues(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, title);
      pstmt.setInt(2, duration);
      pstmt.setInt(3, albumId);
      pstmt.setInt(4, artistId);
      pstmt.setString(5, href);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
