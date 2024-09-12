package ui.controllers;

import java.util.concurrent.*;

import db.SongModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import ui.App;

public class PlayerController {
  @FXML
  private ImageView musicImage;
  @FXML
  private Label musicTitle;
  @FXML
  private Label musicArtist;
  @FXML
  private Button likeButton;
  @FXML
  private Button shareButton;
  @FXML
  private Label currentTime;
  @FXML
  private Label totalTime;
  @FXML
  private Slider timeSlider;
  @FXML
  private Button shuffButton;
  @FXML
  private Button backButton;
  @FXML
  private Button prevButton;
  @FXML
  private Button playButton;
  @FXML
  private Button nextButton;
  @FXML
  private Button forwardButton;
  @FXML
  private Button repeatButton;
  @FXML
  private Button volumeButton;
  @FXML
  private Slider volumeSlider;
  @FXML
  private Button playlistButton;

  public void initialize() {
    // load music info
    handleLoadMusicInfo();

    // set tooltip
    setToolTip();

    // load time
    handleLoadTime();

    // load volume
    handleLoadVolume();

    // control
    handleControl();
  }

  private void handleLoadMusicInfo() {
    // load music info
    App.getMusicManager().getMediaPlayer().setOnPlaying(() -> {
      SongModel song = App.getMusicManager().getCurrentSong();

      // set ui
      musicTitle.setText(song.getTitle());
      musicArtist.setText(song.getArtist().getName());
    });
  }

  private void handleLoadTime() {
    ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

    Runnable loadTime = () -> {
      Platform.runLater(() -> {
        MediaPlayer mediaPlayer = App.getMusicManager().getMediaPlayer();
        Duration time = mediaPlayer.getCurrentTime();
        Duration totalTime = mediaPlayer.getTotalDuration();
        String timeString = String.format("%02d:%02d", (int) (time.toSeconds() / 60), (int) time.toSeconds() % 60);
        String totalString = String.format("%02d:%02d", (int) (totalTime.toSeconds() / 60),
            (int) totalTime.toSeconds() % 60);

        // set ui
        this.currentTime.setText(timeString);
        this.timeSlider.setMin(0);
        this.timeSlider.setMax(totalTime.toSeconds());
        this.timeSlider.setValue(time.toSeconds());
        this.totalTime.setText(totalString);
      });
    };

    scheduled.scheduleAtFixedRate(loadTime, 0, 500, TimeUnit.MILLISECONDS);
  }

  private void handleControl() {
    // play pause loading logic
    playButton.setOnAction(e -> {
      // play music
      System.out.println("Click play");
      App.getMusicManager().playMusic();
    });

    // prev button
    prevButton.setOnAction(e -> {
      // play previous song
      App.getMusicManager();
    });
  }

  private void handleLoadVolume() {
    Platform.runLater(() -> {
      MediaPlayer mediaPlayer = App.getMusicManager().getMediaPlayer();
      volumeSlider.setMin(0);
      volumeSlider.setMax(100);
      volumeSlider.setValue(mediaPlayer.getVolume() * 100);
      volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
        mediaPlayer.setVolume(newVal.doubleValue() / 100);
      });
    });
  }

  private void setToolTip() {
    // info
    likeButton.setTooltip(new Tooltip("Like"));
    shareButton.setTooltip(new Tooltip("Share"));

    // control
    backButton.setTooltip(new Tooltip("Quay lại"));
    forwardButton.setTooltip(new Tooltip("Bài tiếp theo"));
    playButton.setTooltip(new Tooltip("Phát"));
    prevButton.setTooltip(new Tooltip("- 10s"));
    nextButton.setTooltip(new Tooltip("+ 10s"));
    repeatButton.setTooltip(new Tooltip("Lặp lại"));
    shuffButton.setTooltip(new Tooltip("Ngẫu nhiên"));
    volumeButton.setTooltip(new Tooltip("Âm lượng"));
    playlistButton.setTooltip(new Tooltip("Danh sách phát"));
  }

}
