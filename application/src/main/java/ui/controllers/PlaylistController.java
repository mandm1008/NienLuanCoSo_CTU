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
    for (int i = 0; i < playlist.size(); i++) {
      SongModel song = playlist.get(i);

      // load fxml
      try {
        FXMLLoader loader = DefindUI.loadFXML(DefindUI.getPlaylistItem());
        Parent root = loader.load();

        // add to playlist box
        playlistBox.getChildren().add(root);

        // set title, artist, image, play button action, play image
        PlaylistItemController controller = loader.getController();
        controller.setTitle(song.getTitle());
        if (song.getArtistId() <= 0) {
          controller.setArtist(song.getArtistName());
        } else {
          controller.setArtist(song.getArtist().getName());
        }
        controller.setImage(song.getImage());
        controller.setIndex(i);
      } catch (IOException e) {
        continue;
      }
    }
  }
}
