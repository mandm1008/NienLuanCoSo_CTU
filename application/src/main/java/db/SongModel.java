package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongModel extends Model {
  private int songId;
  private String title;
  private int albumId;
  private int artistId;
  private String href;
  private String image;

  public SongModel() {
    this.songId = -1;
    this.title = "";
    this.albumId = -1;
    this.artistId = -1;
    this.href = "";
    this.image = "";
  }

  public SongModel(int songId) {
    this.songId = songId;
  }

  public SongModel(String title, int albumId, int artistId, String href, String image) {
    this.title = new String(title);
    this.albumId = albumId;
    this.artistId = artistId;
    this.href = new String(href);
    this.image = new String(image);
  }

  public SongModel(SongModel song) {
    this.title = new String(song.title);
    this.albumId = song.albumId;
    this.artistId = song.artistId;
    this.href = new String(song.href);
    this.image = new String(song.image);
  }

  @Override
  protected String getInsertString() {
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

  @Override
  protected void setValueInsert(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, title);
      pstmt.setInt(3, albumId);
      pstmt.setInt(4, artistId);
      pstmt.setString(5, href);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean findData() {
    if (songId == -1) {
      return false;
    }

    try {
      ResultSet rs = super.findById();

      if (rs.next()) {
        title = rs.getString("title");
        albumId = rs.getInt("album_id");
        artistId = rs.getInt("artist_id");
        href = rs.getString("href");

        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  public AlbumModel getAlbum() {
    AlbumModel album = new AlbumModel(albumId);
    album.findData();

    return album;
  }

  public int getSongId() {
    return songId;
  }

  public String getTitle() {
    return title;
  }

  public int getAlbumId() {
    return albumId;
  }

  public int getArtistId() {
    return artistId;
  }

  public ArtistModel getArtist() {
    ArtistModel artist = new ArtistModel(artistId);
    artist.findData();

    return artist;
  }

  public String getHref() {
    return href;
  }

  public String getImage() {
    return image;
  }

}
