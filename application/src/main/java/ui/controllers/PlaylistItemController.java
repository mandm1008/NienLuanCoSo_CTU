package ui.controllers;

import javafx.util.Callback;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import modules.ImageManager;
import modules.LoadLater;
import ui.App;

public class PlaylistItemController {
  @FXML
  private HBox box;
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
  @FXML
  private Button removeButton;
  @FXML
  private ImageView removeImage;

  private int index;

  public void initialize() {
    playButton.setOnAction(e -> {
      playImage.setImage(ImageManager.getImage(ImageManager.PAUSE));
      box.getStyleClass().add("playlist-onplay");

      // change song
      if (App.getMusicManager().getIndex() != index)
        App.getMusicManager().changeMusic(index);
    });

    // remove button
    removeBtn();
  }

  private Runnable changePlayImage() {
    return () -> {
      System.out.println("Playlist-item: " + index + " change play image - " + App.getMusicManager().getIndex());
      if (App.getMusicManager().getIndex() == index) {
        playImage.setImage(ImageManager.getImage(ImageManager.PAUSE));
        box.getStyleClass().add("playlist-onplay");
        System.out.println("add class: " + box);
      } else {
        playImage.setImage(ImageManager.getImage(ImageManager.PLAY));
        box.getStyleClass().removeIf(style -> style.equals("playlist-onplay"));
        System.out.println("remove class: " + box);
      }
    };
  }

  public void setTitle(String title) {
    this.title.setText(title);
    Tooltip.install(this.title, new Tooltip(title));
  }

  public void setArtist(String artist) {
    this.artist.setText((artist != null && artist.length() > 0) ? artist : "Unknown");
  }

  public void setImage(String url) {
    // use load later
    Callback<Image, Void> callback = new Callback<Image, Void>() {
      @Override
      public Void call(Image img) {
        Platform.runLater(() -> {
          image.setImage(img);
        });
        return null;
      }
    };

    LoadLater.addLoader(url, callback);
  }

  public void setIndex(int index) {
    this.index = index;

    if (this.index == -999) {
      return;
    }

    // add event on change
    Platform.runLater(changePlayImage());
    App.getMusicManager().addEventOnChange("playlist-item-" + this.index, () -> Platform.runLater(changePlayImage()));
  }

  public void setPlayImage(String url) {
    this.playImage.setImage(new Image(url));
  }

  public void removeBtn() {
    removeButton.setOnAction(e -> {
      removeButton.setDisable(true);
      App.getMusicManager().removeMusic(index);
    });
  }

  public void setActionRemoveBtn(Runnable runnable) {
    removeButton.setOnAction(e -> {
      removeButton.setDisable(true);
      runnable.run();
    });
  }

  public void removePlayBtn() {
    playButton.setOpacity(0);
    playButton.setDisable(true);
  }

  public void setStyleBox(String style) {
    box.setStyle(style);
  }

  public void setImageRemoveButton(Image img) {
    removeImage.setImage(img);
  }
}
