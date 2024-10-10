package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlbumModel extends Model {

  // defind table
  private static final String createTable = "" +
      "CREATE TABLE IF NOT EXISTS Albums (" +
      "album_id INT AUTO_INCREMENT PRIMARY KEY, " +
      "title VARCHAR(255) NOT NULL, " +
      "release_year INT, " +
      "artist_id INT, " +
      "FOREIGN KEY (artist_id) REFERENCES Artists(artist_id))";
  private final String tableName = "Albums";
  private final String idName = "album_id";

  @Override
  protected String getTableName() {
    return tableName;
  }

  @Override
  protected String getIdName() {
    return idName;
  }

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
  protected int getId() {
    return albumId;
  }

  public static String getCreateTable() {
    return createTable;
  }

  @Override
  protected String getInsertString() {
    return "INSERT INTO " + getTableName() + " (title, release_year, artist_id) VALUES (?, ?, ?)";
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

  @Override
  protected boolean checkAccess() {
    // check with title
    try {
      QueryResult queryResult = super.query("SELECT * FROM " + getTableName() + " WHERE title = ?", (pstmt) -> {
        try {
          pstmt.setString(1, title);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });

      if (queryResult.getResultSet().next() == false) {
        queryResult.close();
        return true;
      }

      queryResult.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return false;
  }

  public boolean findData() {
    if (albumId == -1) {
      return false;
    }

    try {
      QueryResult queryResult = super.findById();
      ResultSet rs = queryResult.getResultSet();
      if (rs.next()) {
        this.title = rs.getString("title");
        this.releaseYear = rs.getInt("release_year");
        this.artistId = rs.getInt("artist_id");

        queryResult.close();
        return true;
      }

      queryResult.close();
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
