package modules;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import db.SongModel;

public class MusicManager {

  private MediaPlayer mediaPlayer;
  private SongModel data;

  public MusicManager() {
    this.data = null;
    this.mediaPlayer = null;
  }

  public MusicManager(SongModel song) {
    setSong(song);
    setting();
  }

  public void setSong(SongModel song) {
    this.data = new SongModel(song);
    Media media = new Media(data.getHref());
    this.mediaPlayer = new MediaPlayer(media);
    setting();
  }

  private void setting() {
    this.mediaPlayer.setOnEndOfMedia(() -> {
      this.mediaPlayer.stop();
      this.mediaPlayer.seek(Duration.ZERO);
    });
  }

  public void playMusic() {
    try {
      this.mediaPlayer.play();
    } catch (NullPointerException e) {
      System.out.println("No song to play");
    }
  }

  public void pauseMusic() {
    this.mediaPlayer.pause();
  }

  public void stopMusic() {
    this.mediaPlayer.stop();
  }

  public void setVolume(double volume) {
    this.mediaPlayer.setVolume(volume);
  }

  public double getVolume() {
    return this.mediaPlayer.getVolume();
  }

  public Duration getCurrentTime() {
    return this.mediaPlayer.getCurrentTime();
  }

  public Duration getTotalTime() {
    return this.mediaPlayer.getTotalDuration();
  }
}
