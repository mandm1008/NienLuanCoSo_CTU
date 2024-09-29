package ui.controllers;

import java.io.IOException;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import ui.App;
import ui.DefindUI;
import db.SongModel;

public class PlaylistController {
  @FXML
  private VBox playlistBox;

  public void initialize() {
    loadPlaylistItem();

    // add event on change playlist
    String key = "playlist-ui";
    App.getMusicManager().addEventOnChangePlaylist(key, () -> {
      Platform.runLater(() -> {
        playlistBox.getChildren().clear();
        loadPlaylistItem();
      });
    });
  }

  private void loadPlaylistItem() {
    LinkedList<SongModel> playlist = App.getMusicManager().getPlaylist();

    // add title
    Label title = new Label("Danh sách phát (" + playlist.size() + ")");
    title.getStyleClass().add("playlist-title");
    playlistBox.getChildren().add(title);

    // add playlist item
    for (SongModel song : playlist) {
      // load fxml
      try {
        FXMLLoader loader = DefindUI.loadFXML(DefindUI.getPlaylistItem());
        Parent root = loader.load();

        // add to playlist box
        playlistBox.getChildren().add(root);

        // set title, artist, image, play button action, play image
        PlaylistItemController controller = loader.getController();
        controller.setTitle(song.getTitle());
        controller.setArtist(song.getArtist().getName());
        controller.setImage(song.getImage());
        controller.setIndex(playlist.indexOf(song));
      } catch (IOException e) {
        continue;
      }
    }
  }
}
