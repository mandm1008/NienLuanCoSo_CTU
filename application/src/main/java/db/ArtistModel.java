package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistModel extends Model {

  // defind table
  private static final String createTable = "" +
      "CREATE TABLE IF NOT EXISTS Artists (" +
      "artist_id INT AUTO_INCREMENT PRIMARY KEY, " +
      "name VARCHAR(255) NOT NULL, " +
      "genre VARCHAR(255))";
  private final String tableName = "Artists";
  private final String idName = "artist_id";

  protected static String getCreateTable() {
    return createTable;
  }

  @Override
  protected String getTableName() {
    return tableName;
  }

  @Override
  protected String getIdName() {
    return idName;
  }

  // for model
  private int artistId;
  private String name;
  private String genre;

  public ArtistModel(int artistId) {
    this.artistId = artistId;
  }

  public ArtistModel(String name) {
    this.name = new String(name);
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
  protected int getId() {
    return artistId;
  }

  @Override
  protected String getInsertString() {
    return "INSERT INTO " + getTableName() + " (name) VALUES (?)";
  }

  @Override
  protected void setValueInsert(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, name);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected boolean checkAccess() {
    // check with title
    try {
      if (super.query("SELECT * FROM " + getTableName() + " WHERE name = ?", (pstmt) -> {
        try {
          pstmt.setString(1, name);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }) == null)
        return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return false;
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
      artistId = -1;
      name = "Unkown";
      genre = "Unkown";
    }

    return false;
  }

  public void findByName() {
    try {
      ResultSet rs = query("SELECT * FROM Artists WHERE name = ?", (pstmt) -> {
        try {
          pstmt.setString(1, name);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

      if (rs.next()) {
        this.artistId = rs.getInt("id");
        this.name = rs.getString("name");
        this.genre = rs.getString("genre");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
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
