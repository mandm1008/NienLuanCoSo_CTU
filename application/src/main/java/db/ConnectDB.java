package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectDB {
  // local mysql
  private static final String LOCAL_URL = "jdbc:mysql://localhost:3306/"; // Database URL
  private static final String LOCAL_DBNAME = "your_music"; // Database Name
  private static final String LOCAL_DRIVE = "com.mysql.cj.jdbc.Driver"; // Driver Name
  private static final String LOCAL_USERNAME = "root"; // Database Username
  private static final String LOCAL_PASSWORD = ""; // Database Password

  private static String url = LOCAL_URL; // Database URL
  private static String dbName = LOCAL_DBNAME; // Database Name
  private static String driver = LOCAL_DRIVE; // Driver Name
  private static String userName = LOCAL_USERNAME; // Database Username
  private static String password = LOCAL_PASSWORD; // Database Password

  private Connection conn = null;

  public static boolean loadProperties() {
    Properties properties = new Properties();

    try (FileInputStream input = new FileInputStream("config.properties")) {
      // Load thông tin từ file properties
      properties.load(input);

      String fUrl = properties.getProperty("db.url");
      String fDbName = properties.getProperty("db.name");
      String fUserName = properties.getProperty("db.username");
      String fPassword = properties.getProperty("db.password");

      System.out.println("Url: " + fUrl);
      System.out.println("Database Name: " + fDbName);
      System.out.println("Username: " + fUserName);
      System.out.println("Password: " + fPassword);

      if (fUrl != null && fDbName != null && fUserName != null && fPassword != null) {
        url = fUrl;
        dbName = fDbName;
        userName = fUserName;
        password = fPassword;
      } else {
        return false;
      }

      ConnectDB db = new ConnectDB();

      if (db.getConnect() == null) {
        url = LOCAL_URL;
        dbName = LOCAL_DBNAME;
        userName = LOCAL_USERNAME;
        password = LOCAL_PASSWORD;

        return false;
      }

      db.closeConnect();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

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
    } catch (NullPointerException e) {
      System.out.println("Connection is null");
    }
  }

  public boolean createTable() {
    try {
      conn.createStatement().execute(UserModel.getCreateTable());
      conn.createStatement().execute(ArtistModel.getCreateTable());
      conn.createStatement().execute(AlbumModel.getCreateTable());
      conn.createStatement().execute(PlaylistModel.getCreateTable());
      conn.createStatement().execute(SongModel.getCreateTable());
      conn.createStatement().execute(UserLikes.getCreateTable());
      conn.createStatement().execute(PlaylistSongModel.getCreateTable());
      conn.createStatement().execute(SettingModel.getCreateTable());

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

      // createTable();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      System.out.println("Driver JBDC not found");
      e.printStackTrace();
    }

    return conn;
  }

  public static void initDataDB() {
    ConnectDB db = new ConnectDB();
    db.createTable();

    // init data
    // user 0
    UserModel user0 = new UserModel("system", "system", "system@system.system");
    user0.insert();
    user0.update("UPDATE Users SET user_id = 0 WHERE user_id = ?", (pstm) -> {
      try {
        pstm.setInt(1, 1);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    // user 1
    UserModel user1 = new UserModel("admin", "admin", "admin@admin.admin");
    user1.insert();

    System.out.println("Init data for Users");

    // default songs
    SongModel song1 = new SongModel(
        "Áng mây vô tình",
        "https://drive.google.com/uc?export=download&id=11lSpMvavILLYt1tZVzIME_qXMGLzSu6J",
        "https://drive.google.com/uc?export=download&id=1c8d4_FHjHKi4Uq6cOmH3AiKUckzw7Qsg");
    song1.insert("Lương Gia Hùng");

    SongModel song2 = new SongModel(
        "Cho Mình Em",
        "https://drive.google.com/uc?export=download&id=1fvPeoGrW8YJGPMQOTN6X8Nl7GNr_ttx0",
        "https://drive.google.com/uc?export=download&id=10HfybCtERx8H4Xb-NkmkTYm1tXYcJRWx");
    song2.insert("Binz");

    SongModel song3 = new SongModel(
        "Trốn tìm",
        "https://drive.google.com/uc?export=download&id=1eWvul47FgBXsBxLRbLydZS0lnaG3rDaq",
        "https://drive.google.com/uc?export=download&id=1TFgSXux8yp5_jWPqjJS0y4vuMKm9pYj_");
    song3.insert("Đen Vâu");

    SongModel song4 = new SongModel(
        "Đường tôi chở em về",
        "https://drive.google.com/uc?export=download&id=1e_1nzy-Ywgf1y3OEFxaAA8VqmMtbDSmP",
        "https://drive.google.com/uc?export=download&id=1WXA0HVTOEBlemJwbc0L-WqO6ffygoCtd");
    song4.insert("Buitruonglinh");

    SongModel song5 = new SongModel(
        "Cho tôi lang thang",
        "https://drive.google.com/uc?export=download&id=1FxVjub9ZEiaVc3pNZTcPhhMmPKj7ortp",
        "https://drive.google.com/uc?export=download&id=1aHE0RECh44JCi_0pwMJI52NQyRrfi1LL");
    song5.insert("Đen Vâu");

    SongModel song6 = new SongModel(
        "Người lạ ơi",
        "https://drive.google.com/uc?export=download&id=1kFSy4UQyXUGkL9xeU8cQ1b2_7dZb0231",
        "https://drive.google.com/uc?export=download&id=1YNAkDJO9lVpLQuzfi66IjD5lDzfMh-km");
    song6.insert("Karik");

    SongModel song7 = new SongModel(
        "Người thứ ba",
        "https://drive.google.com/uc?export=download&id=1NzvBCmbXsmYIWz468jNYVwOFIDPM5QNC",
        "https://drive.google.com/uc?export=download&id=17MyHxv67p4HBDAq0L5E5Vr4wpKMoKmc0");
    song7.insert("H2K");

    SongModel song8 = new SongModel(
        "Người lạ ơi",
        "https://drive.google.com/uc?export=download&id=1kFSy4UQyXUGkL9xeU8cQ1b2_7dZb0231",
        "https://drive.google.com/uc?export=download&id=1YNAkDJO9lVpLQuzfi66IjD5lDzfMh-km");
    song8.insert("Karik");

    System.out.println("Init data for Songs");

    // default playlist
    PlaylistModel playlist1 = new PlaylistModel("Mới nhất", 0);
    playlist1.insert();
    // set id 0 for playlist 1
    playlist1.update("UPDATE Playlists SET playlist_id = 0 WHERE playlist_id = ?", (pstm) -> {
      try {
        pstm.setInt(1, 1);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    PlaylistModel playlist2 = new PlaylistModel("Nhiều lượt nghe", 0);
    playlist2.insert();
    // set id 1 for playlist 2
    playlist2.update("UPDATE Playlists SET playlist_id = 1 WHERE playlist_id = ?", (pstm) -> {
      try {
        pstm.setInt(1, 2);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    System.out.println("Init data for Playlists");

    db.closeConnect();
  }

  public static void main(String[] args) {
    loadProperties();
    initDataDB();
  }
}
