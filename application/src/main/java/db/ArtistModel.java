package db;

import java.sql.PreparedStatement;

public class ArtistModel extends Model {
  private int artistId;
  private String name;
  private String genre;

  public ArtistModel(String name, String genre) {
    this.name = name;
    this.genre = genre;
  }

  public ArtistModel(ArtistModel artist) {
    this.name = artist.name;
    this.genre = artist.genre;
  }

  @Override
  protected String getSQLString() {
    return "INSERT INTO Artists (name, genre) VALUES (?, ?)";
  }

  @Override
  protected String getTableName() {
    return "Artists";
  }

  @Override
  protected int getId() {
    return artistId;
  }

  @Override
  protected void setValues(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, name);
      pstmt.setString(2, genre);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
