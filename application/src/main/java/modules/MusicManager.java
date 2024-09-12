package modules;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.List;

import db.PlaylistModel;
import db.SongModel;
import ui.App;

public class MusicManager {

  private MediaPlayer mediaPlayer;
  private PlaylistModel playlistInfo = new PlaylistModel("default", -1);
  private List<SongModel> playlist;
  private int index = 0;

  public MusicManager() {
    this.mediaPlayer = null;
  }

  public MusicManager(SongModel song) {
    setSong(song);
  }

  public MusicManager(List<SongModel> playlist) {
    this.playlist = playlist;
    this.index = 0;
    reLoadData();
  }

  private void setting() {
    this.mediaPlayer.setOnEndOfMedia(() -> {
      this.mediaPlayer.stop();
      this.mediaPlayer.seek(Duration.ZERO);
    });
  }

  private void reLoadData() {
    this.mediaPlayer.stop();
    Media media = new Media(playlist.get(index).getHref());
    this.mediaPlayer = new MediaPlayer(media);
    setting();

    // update ui
    App.reload();
  }

  public void setSong(SongModel song) {
    this.playlist.add(song);
    this.index = playlist.size() - 1;
    reLoadData();
  }

  public void setPlaylist(List<SongModel> playlist) {
    this.playlist = playlist;
    this.index = 0;
    reLoadData();
  }

  public void addToPlaylist(SongModel song) {
    this.playlist.add(song);
  }

  public PlaylistModel getPlaylistInfo() {
    return this.playlistInfo;
  }

  public SongModel getCurrentSong() {
    return this.playlist.get(index);
  }

  public List<SongModel> getPlaylist() {
    return this.playlist;
  }

  public MediaPlayer getMediaPlayer() {
    return this.mediaPlayer;
  }

  private void changeMusic(int index) {
    if (index < 0 || index >= playlist.size()) {
      return;
    }

    this.index = index;
    reLoadData();
    playMusic();
  }

  public void forwardMusic() {
    if (index < playlist.size() - 1) {
      changeMusic(index + 1);
    }
  }

  public void backMusic() {
    if (index > 0) {
      changeMusic(index - 1);
    }
  }

  public void pauseMusic() {
    try {
      this.mediaPlayer.pause();
    } catch (NullPointerException e) {
      System.out.println("No song to pause");
    }
  }

  public void prev10sMusic() {
    try {
      this.mediaPlayer.seek(this.mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
    } catch (NullPointerException e) {
      System.out.println("No song to seek");
    }
  }

  public void next10sMusic() {
    try {
      this.mediaPlayer.seek(this.mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    } catch (NullPointerException e) {
      System.out.println("No song to seek");
    }
  }

  public void playMusic() {
    try {
      this.mediaPlayer.play();
    } catch (NullPointerException e) {
      System.out.println("No song to play");
    }
  }
}
