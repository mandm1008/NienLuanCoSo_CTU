package db;

import java.sql.PreparedStatement;

public class AlbumSchema extends Schema {
  private String title;
  private int releaseYear;
  private int artistId;

  public AlbumSchema(String title, int releaseYear, int artistId) {
    this.title = title;
    this.releaseYear = releaseYear;
    this.artistId = artistId;
  }

  public AlbumSchema(AlbumSchema album) {
    this.title = album.title;
    this.releaseYear = album.releaseYear;
    this.artistId = album.artistId;
  }

  @Override
  protected String getSQLString() {
    return "INSERT INTO Albums (title, release_year, artist_id) VALUES (?, ?, ?)";
  }

  @Override
  protected String getTableName() {
    return "Albums";
  }

  @Override
  protected void setValues(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, title);
      pstmt.setInt(2, releaseYear);
      pstmt.setInt(3, artistId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
