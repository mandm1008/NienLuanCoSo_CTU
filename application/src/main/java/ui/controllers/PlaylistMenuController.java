package ui.controllers;

import db.PlaylistModel;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import modules.MusicManager;
import ui.App;

public class PlaylistMenuController {
  @FXML
  private Label title;
  @FXML
  private Button playButton;

  private PlaylistModel playlist;

  public void setTitle(String title) {
    this.title.setText(title);
  }

  // @SuppressWarnings("exports")
  public void setData(PlaylistModel playlist) {
    this.playlist = playlist;

    load();
  }

  private void load() {
    setTitle(playlist.getName());

    playButton.setOnAction(_ -> {
      playButton.setDisable(true);
      MusicManager musicManager = App.getMusicManager();
      musicManager.setPlaylistInfo(playlist);

      musicManager.addEventOnChange("playlist-menu-playlist-play", () -> {
        Platform.runLater(() -> {
          playButton.setDisable(false);
        });
      });
    });
  }

}
