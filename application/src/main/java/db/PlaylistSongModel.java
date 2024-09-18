package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaylistSongModel extends Model {

  // defind table
  private static final String createTable = "" +
      "CREATE TABLE IF NOT EXISTS Playlist_Songs (" +
      "playlist_song_id INT AUTO_INCREMENT PRIMARY KEY, " +
      "playlist_id INT, " +
      "song_id INT, " +
      "FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id)," +
      "FOREIGN KEY (song_id) REFERENCES Songs(song_id))";
  private final String tableName = "Playlist_Songs";
  private final String idName = "playlist_song_id";

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
  private int playlistSongId;
  private int playlistId;
  private int songId;

  public PlaylistSongModel() {
    this.playlistSongId = -1;
    this.playlistId = -1;
    this.songId = -1;
  }

  public PlaylistSongModel(int playlistSongId) {
    this.playlistSongId = playlistSongId;
  }

  public PlaylistSongModel(int playlistId, int songId) {
    this.playlistId = playlistId;
    this.songId = songId;
  }

  public PlaylistSongModel(PlaylistSongModel playlistSong) {
    this.playlistId = playlistSong.playlistId;
    this.songId = playlistSong.songId;
  }

  @Override
  protected String getInsertString() {
    return "INSERT INTO " + getTableName() + " (playlist_id, song_id) VALUES (?, ?)";
  }

  @Override
  protected int getId() {
    return playlistSongId;
  }

  @Override
  protected void setValueInsert(PreparedStatement pstmt) {
    try {
      pstmt.setInt(1, playlistId);
      pstmt.setInt(2, songId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected boolean checkAccess() {
    // check with title
    try {
      if (super.query("SELECT * FROM " + getTableName() + " WHERE (playlist_id, song_id) = (?, ?)", (pstmt) -> {
        try {
          pstmt.setInt(1, playlistId);
          pstmt.setInt(2, songId);
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
    if (playlistSongId == -1) {
      return false;
    }

    try {
      ResultSet rs = super.findById();
      if (rs.next()) {
        this.playlistId = rs.getInt("playlist_id");
        this.songId = rs.getInt("song_id");
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  public int getPlaylistId() {
    return playlistId;
  }

  public int getSongId() {
    return songId;
  }

  public int getPlaylistSongId() {
    return playlistSongId;
  }

  public PlaylistModel getPlaylist() {
    PlaylistModel playlist = new PlaylistModel(playlistId);
    playlist.findData();

    return playlist;
  }

  public SongModel getSong() {
    SongModel song = new SongModel(songId);
    song.findData();

    return song;
  }
}
