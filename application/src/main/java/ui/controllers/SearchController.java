package ui.controllers;

import java.io.IOException;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import modules.SearchManager;
import ui.App;
import ui.DefindUI;
import db.SongModel;

public class SearchController {
  @FXML
  private Label title;
  @FXML
  private GridPane resultGridPane;

  public void initialize() {
    updateSearch();
    // add event on search
    SearchManager searchManager = App.getSearchManager();
    searchManager.addEventOnSearch("search-ui", () -> {
      Platform.runLater(() -> {
        updateSearch();
      });
    });
  }

  public void updateSearch() {
    SearchManager searchManager = App.getSearchManager();

    // update result
    resultGridPane.getChildren().clear();
    LinkedList<SongModel> songs = searchManager.getSongs();
    if (songs == null) {
      title.setText("Kết quả tìm kiếm \"" + searchManager.getKeySearch() + "\": " + 0 + " bài hát");
      return;
    }
    title.setText("Kết quả tìm kiếm \"" + searchManager.getKeySearch() + "\": " + songs.size() + " bài hát");

    for (int i = 0; i < songs.size(); i++) {
      SongModel song = songs.get(i);

      // load song item
      try {
        FXMLLoader loader = DefindUI.loadFXML(DefindUI.getMusicItem());

        resultGridPane.add(loader.load(), i % 3, (int) (i / 3));

        MusicItemController controller = loader.getController();
        controller.setSong(song);
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }

}
