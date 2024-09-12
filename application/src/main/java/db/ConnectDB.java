package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
  private String url = "jdbc:mysql://localhost:3306/"; // Database URL
  private String dbName = "mymusic"; // Database Name
  private String driver = "com.mysql.cj.jdbc.Driver"; // Driver Name
  private String userName = "root"; // Database Username
  private String password = ""; // Database Password

  private Connection conn = null;

  public ConnectDB() {
    connectDB();
  }

  public Connection getConnect() {
    return conn;
  }

  public void closeConnect() {
    try {
      conn.close();
      System.out.println("Connection closed");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private boolean createTable() {
    String createArtistsTable = "CREATE TABLE IF NOT EXISTS Artists (" +
        "artist_id INT AUTO_INCREMENT PRIMARY KEY, " +
        "name VARCHAR(255) NOT NULL, " +
        "genre VARCHAR(255))";

    String createAlbumsTable = "CREATE TABLE IF NOT EXISTS Albums (" +
        "album_id INT AUTO_INCREMENT PRIMARY KEY, " +
        "title VARCHAR(255) NOT NULL, " +
        "release_year INT, " +
        "artist_id INT, " +
        "FOREIGN KEY (artist_id) REFERENCES Artists(artist_id))";

    String createSongsTable = "CREATE TABLE IF NOT EXISTS Songs (" +
        "song_id INT AUTO_INCREMENT PRIMARY KEY, " +
        "title VARCHAR(255) NOT NULL, " +
        "album_id INT, " +
        "artist_id INT, " +
        "href VARCHAR(2083) NOT NULL," +
        "image VARCHAR(2083) NOT NULL," +
        "FOREIGN KEY (artist_id) REFERENCES Artists(artist_id)," +
        "FOREIGN KEY (album_id) REFERENCES Albums(album_id))";

    String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
        "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
        "username VARCHAR(255) NOT NULL UNIQUE, " +
        "password VARCHAR(255) NOT NULL," +
        "email VARCHAR(255) NOT NULL," +
        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "deleted_at TIMESTAMP NULL DEFAULT NULL)";

    String createPlaylistsTable = "CREATE TABLE IF NOT EXISTS Playlists (" +
        "playlist_id INT AUTO_INCREMENT PRIMARY KEY, " +
        "name VARCHAR(255) NOT NULL, " +
        "user_id INT, " +
        "FOREIGN KEY (user_id) REFERENCES Users(user_id))";

    String createPlaylistSongsTable = "CREATE TABLE IF NOT EXISTS Playlist_Songs (" +
        "playlist_song_id INT AUTO_INCREMENT PRIMARY KEY, " +
        "playlist_id INT, " +
        "song_id INT, " +
        "FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id)," +
        "FOREIGN KEY (song_id) REFERENCES Songs(song_id))";

    try {
      conn.createStatement().execute(createArtistsTable);
      conn.createStatement().execute(createAlbumsTable);
      conn.createStatement().execute(createSongsTable);
      conn.createStatement().execute(createUsersTable);
      conn.createStatement().execute(createPlaylistsTable);
      conn.createStatement().execute(createPlaylistSongsTable);

      System.out.println("Tables created");
      return true;
    } catch (SQLException e) {
      System.out.println("Failed to create tables");
      e.printStackTrace();
      return false;
    }
  }

  private Connection connectDB() {
    try {
      Class.forName(driver);
      conn = DriverManager.getConnection(url + dbName, userName, password);
      System.out.println("Connected to the database");
      createTable();
    } catch (SQLException e) {
      System.out.println("Connection failed");
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      System.out.println("Driver JBDC not found");
      e.printStackTrace();
    }

    return conn;
  }
}
