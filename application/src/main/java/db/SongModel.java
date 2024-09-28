package db;

import java.util.LinkedList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongModel extends Model {

  // defind table
  private static final String createTable = "" +
      "CREATE TABLE IF NOT EXISTS Songs (" +
      "song_id INT AUTO_INCREMENT PRIMARY KEY, " +
      "title VARCHAR(255) NOT NULL, " +
      "album_id INT NULL, " +
      "artist_id INT DEFAULT 0, " +
      "href VARCHAR(2083) NOT NULL," +
      "image VARCHAR(2083) NOT NULL," +
      "view INT DEFAULT 0," +
      "FOREIGN KEY (artist_id) REFERENCES Artists(artist_id)," +
      "FOREIGN KEY (album_id) REFERENCES Albums(album_id) ON DELETE SET NULL)";
  private final String tableName = "Songs";
  private final String idName = "song_id";

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
  private int songId;
  private String title;
  private int albumId;
  private int artistId;
  private String artistName = "Unkown";
  private String href;
  private String image;
  private int view;

  public SongModel() {
    this.songId = -1;
    this.title = "";
    this.albumId = -1;
    this.artistId = -1;
    this.href = "";
    this.image = "";
    this.view = 0;
  }

  public SongModel(int songId) {
    this.songId = songId;
  }

  public SongModel(String title, String href, String image) {
    this.title = new String(title);
    this.href = new String(href);
    this.image = new String(image);
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
    this.view = song.view;
  }

  @Override
  protected String getInsertString() {
    return "INSERT INTO " + getTableName() + " (title, artist_id, href, image) VALUES (?, ?, ?, ?)";
  }

  @Override
  protected int getId() {
    return songId;
  }

  @Override
  protected void setValueInsert(PreparedStatement pstmt) {
    try {
      pstmt.setString(1, title);
      pstmt.setInt(2, artistId);
      pstmt.setString(3, href);
      pstmt.setString(4, image);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected boolean checkAccess() {
    // check with title
    try {
      if (super.query("SELECT * FROM " + getTableName() + " WHERE title = ?", (pstmt) -> {
        try {
          pstmt.setString(1, title);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }).next() == false)
        return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return false;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SongModel) {
      SongModel song = (SongModel) obj;

      return songId == song.songId;
    }

    return false;
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
        image = rs.getString("image");
        view = rs.getInt("view");

        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean insert(String artistName) {
    // create artist
    ArtistModel artist = new ArtistModel(artistName);
    artist.insert();
    artist.findByName();

    artistId = artist.getArtistId();

    return super.insert();
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

  public String getArtistName() {
    return artistName;
  }

  private static SongModel readResultSet(ResultSet rs) {
    SongModel song = new SongModel();

    try {
      song.songId = rs.getInt("song_id");
      song.title = rs.getString("title");
      song.albumId = rs.getInt("album_id");
      song.artistId = rs.getInt("artist_id");
      song.href = rs.getString("href");
      song.image = rs.getString("image");
      song.view = rs.getInt("view");

      ArtistModel artist = song.getArtist();
      if (artist.getName() != null) {
        song.artistName = artist.getName();
      } else {
        song.artistName = "Unknown";
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return song;
  }

  private static LinkedList<SongModel> cacheNewSongs;

  public static LinkedList<SongModel> getNewSongs(int quantity) {
    if (cacheNewSongs != null) {
      return cacheNewSongs;
    }

    LinkedList<SongModel> songs = new LinkedList<SongModel>();
    ConnectDB connectDB = new ConnectDB();

    try {
      String query = "SELECT * FROM Songs ORDER BY song_id DESC LIMIT ?";
      PreparedStatement pstmt = connectDB.getConnect().prepareStatement(query);
      pstmt.setInt(1, quantity);

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        songs.add(readResultSet(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      connectDB.closeConnect();
    }

    // save cache
    cacheNewSongs = songs;

    return songs;
  }

  private static LinkedList<SongModel> cacheMostViewSongs;

  public static LinkedList<SongModel> getMostViewSongs(int quantity) {
    if (cacheMostViewSongs != null) {
      return cacheMostViewSongs;
    }

    LinkedList<SongModel> songs = new LinkedList<SongModel>();
    ConnectDB connectDB = new ConnectDB();

    try {
      String query = "SELECT * FROM Songs ORDER BY view DESC LIMIT ?";
      PreparedStatement pstmt = connectDB.getConnect().prepareStatement(query);
      pstmt.setInt(1, quantity);

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        songs.add(readResultSet(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      connectDB.closeConnect();
    }

    // save cache
    cacheMostViewSongs = songs;

    return songs;
  }

  public void increaseView() {
    view++;
    try {
      super.update("UPDATE Songs SET view = view + 1 WHERE song_id = ?", (pstmt) -> {
        try {
          pstmt.setInt(1, songId);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getView() {
    return view;
  }

}
