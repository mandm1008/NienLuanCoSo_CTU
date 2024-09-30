package ui.controllers;

import javafx.util.Callback;

import java.io.IOException;

import db.SongModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Popup;

import modules.LoadLater;
import ui.DefindUI;

public class MusicItemController {
  @FXML
  private ImageView image;
  @FXML
  private Label title;
  @FXML
  private Label artist;
  @FXML
  private Label view;
  @FXML
  private Button menuButton;

  private SongModel songData;
  private Popup menuPopup;

  public void initialize() {
  }

  private void handleMenuButton() {
    menuPopup = new Popup();
    menuPopup.setAutoHide(true);

    // load menu
    try {
      FXMLLoader loader = DefindUI.loadFXML(DefindUI.getMusicMenu());
      menuPopup.getContent().add(loader.load());

      MusicMenuController controller = loader.getController();
      controller.setSong(songData);
    } catch (IOException e) {
      e.printStackTrace();
    }

    menuButton.setOnAction(e -> {
      if (menuPopup.isShowing()) {
        menuPopup.hide();
      } else {
        double x = menuButton.localToScreen(menuButton.getBoundsInLocal()).getMaxX();
        double y = menuButton.localToScreen(menuButton.getBoundsInLocal()).getMinY();

        menuPopup.show(menuButton.getScene().getWindow(), x, y);
      }
    });
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
    songData = song;
    setTitle(song.getTitle());
    setArtist(song.getArtist().getName());
    setView(song.getView());
    setImage(song.getImage());

    // handle menu button
    handleMenuButton();
  }
}
