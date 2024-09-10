package ui.controllers;

import java.util.concurrent.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
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
    // load time
    ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

    Runnable loadTime = () -> {
      Platform.runLater(() -> {
        Duration time = App.getMusicManager().getCurrentTime();
        Duration totalTime = App.getMusicManager().getTotalTime();
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

    scheduled.scheduleAtFixedRate(loadTime, 0, 1000, TimeUnit.MILLISECONDS);

    // play pause loading logic
    playButton.setOnAction(e -> {
      // play music
      System.out.println("Click play");
      App.getMusicManager().playMusic();
    });

    // load volume
    Platform.runLater(() -> {
      volumeSlider.setMin(0);
      volumeSlider.setMax(100);
      volumeSlider.setValue(App.getMusicManager().getVolume() * 100);
      volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
        App.getMusicManager().setVolume(newVal.doubleValue() / 100);
      });
    });

    // pauseButton.setOnAction(e -> {
    // // pause music
    // App.getMusicManager().pauseMusic();
    // });

    prevButton.setOnAction(e -> {
      // play previous song
      App.getMusicManager();
    });
  }

}
