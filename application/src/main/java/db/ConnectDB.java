package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private boolean createTable() {
    try {
      conn.createStatement().execute(UserModel.getCreateTable());
      conn.createStatement().execute(ArtistModel.getCreateTable());
      conn.createStatement().execute(AlbumModel.getCreateTable());
      conn.createStatement().execute(PlaylistModel.getCreateTable());
      conn.createStatement().execute(SongModel.getCreateTable());
      conn.createStatement().execute(UserLikes.getCreateTable());
      conn.createStatement().execute(PlaylistSongModel.getCreateTable());

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

      // set Timeout
      ExecutorService executor = Executors.newSingleThreadExecutor();
      conn.setNetworkTimeout(executor, 10000);

      createTable();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      System.out.println("Driver JBDC not found");
      e.printStackTrace();
    }

    return conn;
  }
}
