package ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;

import db.SongModel;
import db.UserModel;
import ui.App;
import ui.DefindUI;
import modules.AccountManager;
import modules.ImageManager;
import modules.LoadLater;
import modules.ThreadCustom;

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
  private ImageView likeImage;
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
  private ImageView playImage;
  @FXML
  private Button nextButton;
  @FXML
  private Button forwardButton;
  @FXML
  private Button repeatButton;
  @FXML
  private Button volumeButton;
  @FXML
  private ImageView volumeImage;
  @FXML
  private Slider volumeSlider;
  @FXML
  private Button playlistButton;

  public void initialize() {
    System.out.println("Load Player");
    // load music info
    handleLoadMusicInfo().run();
    App.getMusicManager().addEventOnChange("load-music-info", handleLoadMusicInfo());

    // load liked
    handleLoadLiked().run();
    App.getMusicManager().addEventOnChange("load-liked", handleLoadLiked());

    // share
    handleShare();

    // set tooltip
    setToolTip();

    // load time
    handleLoadTime().run();
    String keyLoadTime = "load-time-player";
    ThreadCustom.stop(keyLoadTime);
    ThreadCustom threadTime = new ThreadCustom(1, 400, 0);
    threadTime.runner(keyLoadTime, handleLoadTime());

    // control
    handleControl().run();
    App.getMusicManager().addEventOnPlay("play-control", handleControl());
    App.getMusicManager().addEventOnLoad("play-button-load", handleLoadingState());

    // load repeat
    handleRepeat().run();

    // load shuffle
    handleShuffle().run();

    // load volume
    handleLoadVolume().run();
    App.getMusicManager().addEventOnChange("load-volume", handleLoadVolume());

    // load playlist
    Platform.runLater(() -> {
      handlePlaylist();
    });
  }

  private Runnable handleLoadMusicInfo() {
    return () -> {
      System.out.println("Set: " + App.getMusicManager().getCurrentSong().getTitle());
      SongModel song = App.getMusicManager().getCurrentSong();

      // set ui
      Callback<Image, Void> callback = new Callback<Image, Void>() {
        @Override
        public Void call(Image img) {
          Platform.runLater(() -> {
            musicImage.setImage(img);
          });
          return null;
        }
      };
      LoadLater.addLoader(song.getImage(), callback);
      Platform.runLater(() -> {
        musicTitle.setText(song.getTitle());
        musicArtist.setText(song.getArtistName());
      });
    };
  }

  private Runnable handleLoadLiked() {
    // check login
    if (AccountManager.getId() < 0) {
      likeImage.setImage(ImageManager.getImage(ImageManager.LIKE));
    }

    // like button
    likeButton.setOnAction(e -> {
      if (AccountManager.getId() < 0) {
        return;
      }
      SongModel song = App.getMusicManager().getCurrentSong();
      UserModel user = new UserModel(AccountManager.getId());

      if (user.checkLikedSong(song.getSongId())) {
        user.unlikeSong(song.getSongId());
        likeImage.setImage(ImageManager.getImage(ImageManager.LIKE));
      } else {
        user.likeSong(song.getSongId());
        likeImage.setImage(ImageManager.getImage(ImageManager.LIKED));
      }
    });

    return () -> {
      SongModel song = App.getMusicManager().getCurrentSong();
      UserModel user = new UserModel(AccountManager.getId());

      if (user.checkLikedSong(song.getSongId())) {
        likeImage.setImage(ImageManager.getImage(ImageManager.LIKED));
      } else {
        likeImage.setImage(ImageManager.getImage(ImageManager.LIKE));
      }
    };
  }

  private void handleShare() {
    // share button
    shareButton.setOnAction(e -> {
      // share music
      SongModel song = App.getMusicManager().getCurrentSong();
      System.out.println("Share: " + song.getHref());
    });
  }

  private Runnable handleLoadTime() {
    // time slider
    timeSlider.setOnMouseReleased(event -> {
      MediaPlayer mediaPlayer = App.getMusicManager().getMediaPlayer();
      mediaPlayer.seek(Duration.seconds(Math.round(timeSlider.getValue())));
    });

    // Tooltip for Slider
    Tooltip tooltip = new Tooltip();

    // Link Tooltip - Slider
    Tooltip.install(timeSlider, tooltip);

    timeSlider.setOnMouseMoved(event -> {
      // calc time
      double mouseX = event.getX();
      double sliderMin = timeSlider.getMin();
      double sliderMax = timeSlider.getMax();
      double sliderWidth = timeSlider.getWidth() - 4;

      double value = sliderMin + (mouseX / sliderWidth) * (sliderMax - sliderMin);

      int totalSeconds = (int) value;
      int minutes = totalSeconds / 60;
      int seconds = totalSeconds % 60;

      // Format "mm:ss"
      String timeFormatted = String.format("%02d:%02d", minutes, seconds);

      tooltip.setText(timeFormatted);
      tooltip.setStyle("-fx-font-size: 12px; -fx-text-fill: #fff;-fx-font-weight: bold;");

      tooltip.show(timeSlider, event.getScreenX(), event.getScreenY() + 15);
    });

    // hide tooltip
    timeSlider.setOnMouseExited(event -> {
      tooltip.hide();
    });

    return () -> {
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
    };

  }

  private Runnable handleControl() {
    // play pause loading logic
    playButton.setOnAction(e -> {
      // play or pause music
      if (App.getMusicManager().getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
        App.getMusicManager().pauseMusic();
        playImage.setImage(ImageManager.getImage(ImageManager.PLAY));
      } else {
        App.getMusicManager().playMusic();
        playImage.setImage(ImageManager.getImage(ImageManager.PAUSE));
      }
    });

    // prev button
    prevButton.setOnAction(e -> {
      // play previous song
      App.getMusicManager().prev10sMusic();
    });

    // next button
    nextButton.setOnAction(e -> {
      // play next song
      App.getMusicManager().next10sMusic();
    });

    // forward button
    forwardButton.setOnAction(e -> {
      // forward music
      App.getMusicManager().forwardMusic();
    });

    // back button
    backButton.setOnAction(e -> {
      // back music
      App.getMusicManager().backMusic();
    });

    return () -> {
      MediaPlayer mediaPlayer = App.getMusicManager().getMediaPlayer();
      if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
        playImage.setImage(ImageManager.getImage(ImageManager.PAUSE));
      } else {
        playImage.setImage(ImageManager.getImage(ImageManager.PLAY));
      }

      // active button
      playButton.setDisable(false);
    };
  }

  private Runnable handleLoadingState() {
    return () -> {
      playButton.setDisable(true);
      playImage.setImage(ImageManager.getImage(ImageManager.LOADING));
    };
  }

  private Runnable handleRepeat() {
    repeatButton.setOnAction(e -> {
      // repeat music
      if (App.getMusicManager().isRepeat()) {
        App.getMusicManager().setRepeat(false);
        repeatButton.setStyle("-fx-opacity: 0.5;");
      } else {
        App.getMusicManager().setRepeat(true);
        repeatButton.setStyle("-fx-opacity: 1;");
      }
    });

    return () -> {
      if (App.getMusicManager().isRepeat()) {
        repeatButton.setStyle("-fx-opacity: 1;");
      } else {
        repeatButton.setStyle("-fx-opacity: 0.5;");
      }
    };
  }

  private Runnable handleShuffle() {
    shuffButton.setOnAction(e -> {
      // shuffle music
      if (App.getMusicManager().isShuffle()) {
        App.getMusicManager().setShuffle(false);
        shuffButton.setStyle("-fx-opacity: 0.5;");
      } else {
        App.getMusicManager().setShuffle(true);
        shuffButton.setStyle("-fx-opacity: 1;");
      }
    });

    return () -> {
      if (App.getMusicManager().isShuffle()) {
        shuffButton.setStyle("-fx-opacity: 1;");
      } else {
        shuffButton.setStyle("-fx-opacity: 0.5;");
      }
    };
  }

  private Runnable handleLoadVolume() {
    volumeButton.setOnAction(e -> {
      // change icon
      if (volumeSlider.getValue() > 0) {
        volumeSlider.setValue(0);
        volumeImage.setImage(ImageManager.getImage(ImageManager.VOLUMEOFF));
      } else {
        volumeSlider.setValue(80);
        volumeImage.setImage(ImageManager.getImage(ImageManager.VOLUMEON));
      }
    });

    return () -> {
      MediaPlayer mediaPlayer = App.getMusicManager().getMediaPlayer();
      volumeSlider.setMin(0);
      volumeSlider.setMax(100);
      volumeSlider.setValue(mediaPlayer.getVolume() * 100);
      volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
        mediaPlayer.setVolume(newVal.doubleValue() / 100);
        if (newVal.doubleValue() == 0) {
          volumeImage.setImage(ImageManager.getImage(ImageManager.VOLUMEOFF));
        } else {
          volumeImage.setImage(ImageManager.getImage(ImageManager.VOLUMEON));
        }
      });
    };
  }

  private void handlePlaylist() {
    // load playlist
    try {
      ScrollPane playlist = DefindUI.loadFXML(DefindUI.getPlaylist()).load();

      // Ẩn thanh cuộn dọc
      playlist.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

      playlist.setOnScroll(event -> {
        double deltaY = event.getDeltaY() * 0.005;
        playlist.setVvalue(playlist.getVvalue() - deltaY);
      });

      Popup popup = new Popup();
      popup.setAutoHide(true);
      popup.getContent().add(playlist);

      playlistButton.setOnAction(e -> {
        if (popup.isShowing()) {
          popup.hide();
        } else {
          Stage primaryStage = App.getPrimaryStage();
          double windowHeight = primaryStage.getHeight();
          double windowWidth = primaryStage.getWidth();

          double anchorY = 60 + 60;
          double anchorX = windowWidth - playlist.getWidth() - 20;

          double popupHeight = windowHeight - 60 - 60 - 160;
          playlist.setPrefHeight(popupHeight);

          popup.show(primaryStage, anchorX, anchorY);
        }
      });
    } catch (IOException e) {
      System.out.println("Error: load playlist");
      return;
    }
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
