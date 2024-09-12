package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ArtistModel extends Model {
  private int artistId;
  private String name;
  private String genre;

  public ArtistModel(int artistId) {
    this.artistId = artistId;
  }

  public ArtistModel(String name, String genre) {
    this.name = new String(name);
    this.genre = new String(genre);
  }

  public ArtistModel(ArtistModel artist) {
    this.name = new String(artist.name);
    this.genre = new String(artist.genre);
  }

  @Override
  protected String getInsertString() {
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
  protected void setValueInsert(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, name);
      pstmt.setString(2, genre);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean findData() {
    if (artistId == -1) {
      return false;
    }

    try {
      ResultSet rs = super.findById();
      if (rs.next()) {
        this.name = rs.getString("name");
        this.genre = rs.getString("genre");
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  public int getArtistId() {
    return artistId;
  }

  public String getName() {
    return name;
  }

  public String getGenre() {
    return genre;
  }

}
