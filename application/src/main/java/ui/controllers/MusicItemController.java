package ui.controllers;

import javafx.util.Callback;
import db.SongModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import modules.LoadLater;

public class MusicItemController {
  @FXML
  private ImageView image;
  @FXML
  private Button playButton;
  @FXML
  private ImageView playImage;
  @FXML
  private Label title;
  @FXML
  private Label artist;
  @FXML
  private Label view;
  @FXML
  private Button menuButton;

  public void initialize() {

  }

  public void setTitle(String title) {
    this.title.setText(title);
  }

  public void setArtist(String artist) {
    this.artist.setText(artist);
  }

  public void setView(int view) {
    this.view.setText("" + view);
  }

  public void setImage(String src) {
    Callback<Image, Void> callback = new Callback<Image, Void>() {
      @Override
      public Void call(Image img) {
        Platform.runLater(() -> {
          image.setImage(img);
        });
        return null;
      }
    };

    LoadLater.addLoader(src, callback);
  }

  @SuppressWarnings("exports")
  public void setSong(SongModel song) {
    setTitle(song.getTitle());
    setArtist(song.getArtist().getName());
    setView(song.getView());
    setImage(song.getImage());
  }
}
