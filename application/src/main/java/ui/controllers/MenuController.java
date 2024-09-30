package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.App;
import ui.DefindUI;

public class MenuController {
  @FXML
  private Button exploreButton;
  @FXML
  private Button searchButton;
  @FXML
  private Button favoriteButton;
  @FXML
  private Button albumsButton;
  @FXML
  private Button playlistsButton;
  @FXML
  private Button createPlaylistButton;

  public void initialize() {
    // explore button
    String exploreKey = "explore-menu";
    handleExploreButton().run();
    App.addEventChangePage(exploreKey, handleExploreButton());

    // search button
    String searchKey = "search-menu";
    handleSearchButton().run();
    App.addEventChangePage(searchKey, handleSearchButton());
  }

  private Runnable handleExploreButton() {
    exploreButton.setOnAction(e -> {
      if (App.getCurrentContent() != DefindUI.getHome()) {
        App.redirect(DefindUI.getHome());
      }
    });

    return () -> {
      if (App.getCurrentContent() == DefindUI.getHome()) {
        exploreButton.getStyleClass().add("selected");
      } else {
        exploreButton.getStyleClass().remove("selected");
      }
    };
  }

  private Runnable handleSearchButton() {
    searchButton.setOnAction(e -> {
      if (App.getCurrentContent() != DefindUI.getSearch()) {
        App.redirect(DefindUI.getSearch());
      }
    });

    return () -> {
      if (App.getCurrentContent() == DefindUI.getSearch()) {
        searchButton.getStyleClass().add("selected");
      } else {
        searchButton.getStyleClass().remove("selected");
      }
    };
  }
}
