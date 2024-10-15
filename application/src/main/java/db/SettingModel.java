package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingModel extends Model {

  // defind table
  private static final String createTable = "" +
      "CREATE TABLE Settings (" +
      "setting_id INT PRIMARY KEY AUTO_INCREMENT," +
      "playlist_id INT NULL," +
      "shuff BOOLEAN," +
      "play_now BOOLEAN," +
      "volume INT CHECK (volume BETWEEN 0 AND 100) DEFAULT 100," +
      "user_id INT," +
      "FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id) ON DELETE SET NULL," +
      "FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE);";
  private final String tableName = "Settings";
  private final String idName = "setting_id";

  protected static String getCreateTable() {
    return createTable;
  }

  // for model
  private int settingId = -1;
  private int playlistId = -1;
  private boolean shuff = false;
  private boolean playNow = false;
  private int volume = 100;
  private int userId = -1;

  public SettingModel() {
    this.settingId = -1;
    this.playlistId = -1;
    this.shuff = false;
    this.playNow = false;
    this.volume = 100;
    this.userId = -1;
  }

  public SettingModel(int userId) {
    this.userId = userId;
  }

  @Override
  protected String getInsertString() {
    return "INSERT INTO " + tableName + " (user_id) VALUES (?)";
  }

  @Override
  protected String getTableName() {
    return tableName;
  }

  @Override
  protected String getIdName() {
    return idName;
  }

  @Override
  protected int getId() {
    return settingId;
  }

  @Override
  protected void setValueInsert(PreparedStatement pstmt) {
    try {
      pstmt.setInt(1, userId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected boolean checkAccess() {
    try {
      QueryResult qr = super.query("SELECT * FROM " + tableName + " WHERE user_id = ?", (pstmt) -> {
        try {
          pstmt.setInt(1, userId);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });

      if (!qr.getResultSet().next()) {
        qr.close();
        return true;
      }

      qr.close();
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  public int getPlaylistId() {
    return playlistId;
  }

  public boolean getShuff() {
    return shuff;
  }

  public boolean getPlayNow() {
    return playNow;
  }

  public int getVolume() {
    return volume;
  }

  public void readResultSet(ResultSet rs) throws SQLException {
    settingId = rs.getInt("setting_id");
    playlistId = rs.getInt("playlist_id");
    shuff = rs.getBoolean("shuff");
    playNow = rs.getBoolean("play_now");
    volume = rs.getInt("volume");
    userId = rs.getInt("user_id");
  }

  public static SettingModel findDataByUserId(int userId) {
    SettingModel setting = new SettingModel(userId);
    QueryResult qr = setting.query("SELECT * FROM " + setting.tableName + " WHERE user_id = ?", (pstmt) -> {
      try {
        pstmt.setInt(1, userId);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    try {
      if (qr.getResultSet().next()) {
        setting.readResultSet(qr.getResultSet());

        qr.close();
        return setting;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    qr.close();
    return null;
  }

  public PlaylistModel getPlaylist() {
    PlaylistModel playlist = new PlaylistModel(playlistId);

    if (playlistId > 0) {
      playlist.findData();
      return playlist;
    }

    return null;
  }

  public boolean updatePlaylist(int playlistId) {
    this.playlistId = playlistId;

    return super.update("UPDATE " + tableName + " SET playlist_id = ? WHERE user_id = ?", (pstmt) -> {
      try {
        pstmt.setInt(1, playlistId);
        pstmt.setInt(2, userId);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  public boolean updateAll() {
    return super.update(
        "UPDATE " + tableName + " SET playlist_id = ?, shuff = ?, play_now = ?, volume = ? WHERE user_id = ?",
        (pstmt) -> {
          try {
            pstmt.setInt(1, playlistId);
            pstmt.setBoolean(2, shuff);
            pstmt.setBoolean(3, playNow);
            pstmt.setInt(4, volume);
            pstmt.setInt(5, userId);
          } catch (SQLException e) {
            e.printStackTrace();
          }
        });
  }

  public void setPlaylistId(int playlistId) {
    this.playlistId = playlistId;
  }

  public void setShuff(boolean shuff) {
    this.shuff = shuff;
  }

  public void setPlayNow(boolean playNow) {
    this.playNow = playNow;
  }

  public void setVolume(int volume) {
    this.volume = volume;
  }

  public static void main(String[] args) {
    ConnectDB connectDB = new ConnectDB();

    try {
      connectDB.getConnect().createStatement().execute(getCreateTable());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
