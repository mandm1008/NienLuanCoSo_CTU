package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

  public void setImage(String image) {
    this.image.setImage(new Image(image));
  }
}
