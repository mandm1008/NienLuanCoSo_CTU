package ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import modules.ImageManager;
import ui.App;

public class PlaylistItemController {

  @FXML
  private Label title;
  @FXML
  private Label artist;
  @FXML
  private ImageView image;
  @FXML
  private Button playButton;
  @FXML
  private ImageView playImage;

  private int index;

  public void initialize() {
    playButton.setOnAction(e -> {
      playImage.setImage(ImageManager.getImage(ImageManager.PAUSE));
      // change song
      if (App.getMusicManager().getIndex() != index)
        App.getMusicManager().changeMusic(index);
    });

    // change play image
    Platform.runLater(changePlayImage());
    Platform.runLater(() -> {
      App.getMusicManager().addEventOnChange("playlist-item-" + index, changePlayImage());
    });
  }

  private Runnable changePlayImage() {
    return () -> {
      if (App.getMusicManager().getIndex() == index) {
        playImage.setImage(ImageManager.getImage(ImageManager.PAUSE));
      } else {
        playImage.setImage(ImageManager.getImage(ImageManager.PLAY));
      }
    };
  }

  public void setTitle(String title) {
    this.title.setText(title);
  }

  public void setArtist(String artist) {
    this.artist.setText((artist != null && artist.length() > 0) ? artist : "Unknown");
  }

  public void setImage(String url) {
    this.image.setImage(new Image(url));
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public void setPlayImage(String url) {
    this.playImage.setImage(new Image(url));
  }
}
