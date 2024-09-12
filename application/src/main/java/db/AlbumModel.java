package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AlbumModel extends Model {
  private int albumId;
  private String title;
  private int releaseYear;
  private int artistId;

  public AlbumModel() {
    this.albumId = -1;
    this.title = "";
    this.releaseYear = -1;
    this.artistId = -1;
  }

  public AlbumModel(int albumId) {
    this.albumId = albumId;
  }

  public AlbumModel(String title, int releaseYear, int artistId) {
    this.title = new String(title);
    this.releaseYear = releaseYear;
    this.artistId = artistId;
  }

  public AlbumModel(AlbumModel album) {
    this.title = new String(album.title);
    this.releaseYear = album.releaseYear;
    this.artistId = album.artistId;
  }

  @Override
  protected String getInsertString() {
    return "INSERT INTO Albums (title, release_year, artist_id) VALUES (?, ?, ?)";
  }

  @Override
  protected String getTableName() {
    return "Albums";
  }

  @Override
  protected int getId() {
    return albumId;
  }

  @Override
  protected void setValueInsert(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, title);
      pstmt.setInt(2, releaseYear);
      pstmt.setInt(3, artistId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean findData() {
    if (albumId == -1) {
      return false;
    }

    try {
      ResultSet rs = super.findById();
      if (rs.next()) {
        this.title = rs.getString("title");
        this.releaseYear = rs.getInt("release_year");
        this.artistId = rs.getInt("artist_id");
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  public String getTitle() {
    return title;
  }

  public int getReleaseYear() {
    return releaseYear;
  }

  public int getArtistId() {
    return artistId;
  }

  public ArtistModel getArtist() {
    ArtistModel artist = new ArtistModel(artistId);
    artist.findData();

    return artist;
  }

  public int getAlbumId() {
    return albumId;
  }

}
