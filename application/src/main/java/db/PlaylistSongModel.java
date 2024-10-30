package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.nio.file.Path;

import modules.YoutubeData;
import modules.YoutubeDownloader;

public class PlaylistSongModel extends Model {

  // defind table
  private static final String createTable = "" +
      "CREATE TABLE IF NOT EXISTS Playlist_Songs (" +
      "playlist_song_id INT AUTO_INCREMENT PRIMARY KEY, " +
      "playlist_id INT NOT NULL, " +
      "song_id INT, " +
      "external VARCHAR(255), " +
      "FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id) ON DELETE CASCADE," +
      "FOREIGN KEY (song_id) REFERENCES Songs(song_id) ON DELETE CASCADE)";
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
  private String external = null;

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

  public PlaylistSongModel(int playlistId, String external) {
    this.playlistId = playlistId;
    this.external = external;
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
      QueryResult qr = super.query("SELECT * FROM " + getTableName() + " WHERE (playlist_id, song_id) = (?, ?)",
          (pstmt) -> {
            try {
              pstmt.setInt(1, playlistId);
              pstmt.setInt(2, songId);
            } catch (SQLException e) {
              e.printStackTrace();
            }
          });

      if (qr.getResultSet().next() == false) {
        qr.close();
        return true;
      }

      qr.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return false;
  }

  public boolean insertExternal() {
    try {
      return super.update("INSERT INTO " + getTableName() + " (playlist_id, external) VALUES (?, ?)", (pstmt) -> {
        try {
          pstmt.setInt(1, playlistId);
          pstmt.setString(2, external);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean findData() {
    if (playlistSongId == -1) {
      return false;
    }

    try {
      QueryResult qr = super.findById();
      ResultSet rs = qr.getResultSet();
      if (rs.next()) {
        this.playlistId = rs.getInt("playlist_id");
        this.songId = rs.getInt("song_id");
        this.external = rs.getString("external");

        qr.close();
        return true;
      }

      qr.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return false;
  }

  public boolean getDataBySongIdAndPlaylistId() {
    try {
      QueryResult qr = super.query("SELECT * FROM " + getTableName() + " WHERE playlist_id = ? AND song_id = ?",
          (pstmt) -> {
            try {
              pstmt.setInt(1, playlistId);
              pstmt.setInt(2, songId);
            } catch (SQLException e) {
              e.printStackTrace();
            }
          });
      ResultSet rs = qr.getResultSet();

      if (rs.next()) {
        this.playlistSongId = rs.getInt("playlist_song_id");

        qr.close();
        return true;
      }

      qr.close();
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

  public String getExternal() {
    return external;
  }

  public boolean isExternal() {
    return external != null;
  }

  public LinkedList<SongModel> getSongsByPlaylistId(int playlistId) {
    LinkedList<SongModel> songs = new LinkedList<>();

    try {
      QueryResult qr = super.query("SELECT * FROM " + getTableName() + " WHERE playlist_id = ?", (pstmt) -> {
        try {
          pstmt.setInt(1, playlistId);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
      ResultSet rs = qr.getResultSet();

      while (rs.next()) {
        String exString = rs.getString("external");

        if (exString != null) {
          YoutubeData ytData = YoutubeData.getYoutubeInfo(exString);
          String thumbnailLocal = YoutubeDownloader.downloadThumbnail(ytData.getThumbnail(), ytData.getTitle());
          SongModel s = new SongModel(ytData.getTitle(), ytData.getChannelTitle(), ytData.getHrefLocal(),
              Path.of(thumbnailLocal).toUri().toString());
          s.setExternal(exString);
          songs.add(s);
          System.out.println("External song: " + s.getTitle() + " - " + ytData.getChannelTitle());
          continue;
        }

        int songId = rs.getInt("song_id");
        SongModel song = new SongModel(songId);
        song.findData();
        songs.add(song);
      }

      qr.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return songs;
  }

  public boolean deletePlaylist(int playlistId) {
    try {
      return super.update("DELETE FROM " + getTableName() + " WHERE playlist_id = ?", (pstmt) -> {
        try {
          pstmt.setInt(1, playlistId);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean copyPlaylist(int fromPlaylistId, int toPlaylistId) {
    LinkedList<SongModel> songs = getSongsByPlaylistId(fromPlaylistId);

    for (SongModel song : songs) {
      if (song.getSongId() <= 0) {
        PlaylistSongModel playlistSong = new PlaylistSongModel(toPlaylistId, song.getExternal());
        if (!playlistSong.insertExternal()) {
          return false;
        }
        continue;
      }

      PlaylistSongModel playlistSong = new PlaylistSongModel(toPlaylistId, song.getSongId());
      if (!playlistSong.insert()) {
        return false;
      }
    }

    return true;
  }
}
