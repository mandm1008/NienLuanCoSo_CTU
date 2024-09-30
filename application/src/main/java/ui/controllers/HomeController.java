package ui.controllers;

import java.io.IOException;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import ui.DefindUI;
import db.SongModel;

public class HomeController {
  @FXML
  private GridPane releaseGridPane;
  @FXML
  private GridPane mostViewGridPane;

  public void initialize() {
    new Thread(() -> {
      try {
        loadRelease();
        loadMostView();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
  }

  public void loadRelease() {
    LinkedList<SongModel> songs = SongModel.getNewSongs(9);

    Platform.runLater(() -> {
      for (int i = 0; i < songs.size(); i++) {
        try {
          FXMLLoader fxmlLoader = DefindUI.loadFXML(DefindUI.getMusicItem());
          // load fxml
          releaseGridPane.add(fxmlLoader.load(), i % 3, (int) i / 3);

          // set data
          MusicItemController musicItemController = fxmlLoader.getController();
          musicItemController.setSong(songs.get(i));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void loadMostView() throws IOException {
    LinkedList<SongModel> songs = SongModel.getMostViewSongs(9);

    Platform.runLater(() -> {
      for (int i = 0; i < songs.size(); i++) {
        try {
          FXMLLoader fxmlLoader = DefindUI.loadFXML(DefindUI.getMusicItem());
          // load fxml
          mostViewGridPane.add(fxmlLoader.load(), i % 3, (int) i / 3);

          // set data
          MusicItemController musicItemController = fxmlLoader.getController();
          musicItemController.setSong(songs.get(i));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
