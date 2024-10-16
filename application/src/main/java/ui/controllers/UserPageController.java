package ui.controllers;

import java.io.IOException;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import modules.AccountManager;
import ui.DefindUI;
import db.SongModel;

public class UserPageController {
  @FXML
  private Label mySongsTitle;
  @FXML
  private GridPane mySongsGridPane;
  @FXML
  private Label likedTitle;
  @FXML
  private GridPane likedGridPane;

  public void initialize() {
    new Thread(() -> {
      try {
        loadMySong();
        loadLiked();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
  }

  public void loadMySong() {
    LinkedList<SongModel> songs = AccountManager.getSongs();

    Platform.runLater(() -> {
      // load title
      mySongsTitle.setText("Nhạc Của Tôi (" + songs.size() + ")");

      // load songs
      for (int i = 0; i < songs.size(); i++) {
        try {
          FXMLLoader fxmlLoader = DefindUI.loadFXML(DefindUI.getMusicItem());
          // load fxml
          mySongsGridPane.add(fxmlLoader.load(), i % 3, (int) i / 3);

          // set data
          MusicItemController musicItemController = fxmlLoader.getController();
          musicItemController.setSrcMenu(DefindUI.getMusicUserMenu());
          musicItemController.setSong(songs.get(i));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void loadLiked() throws IOException {
    LinkedList<SongModel> songs = AccountManager.getLikedSongs();

    Platform.runLater(() -> {
      // load title
      likedTitle.setText("Nhạc Yêu Thích (" + songs.size() + ")");

      // load songs
      for (int i = 0; i < songs.size(); i++) {
        try {
          FXMLLoader fxmlLoader = DefindUI.loadFXML(DefindUI.getMusicItem());
          // load fxml
          likedGridPane.add(fxmlLoader.load(), i % 3, (int) i / 3);

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
