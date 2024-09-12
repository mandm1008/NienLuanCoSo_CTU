package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PlaylistSongModel extends Model {
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
    return "INSERT INTO PlaylistSongs (playlist_id, song_id) VALUES (?, ?)";
  }

  @Override
  protected String getTableName() {
    return "PlaylistSongs";
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
