package ui.controllers;

import java.io.IOException;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import db.PlaylistModel;
import modules.AccountManager;
import modules.CustomDialog;
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
  private Button playlistsButton;
  @FXML
  private GridPane playlistBox;
  @FXML
  private Button createPlaylistButton;
  @FXML
  private ScrollPane scrollPlaylist;

  public void initialize() {
    // explore button
    String exploreKey = "explore-menu";
    handleExploreButton().run();
    App.addEventChangePage(exploreKey, handleExploreButton());

    // search button
    String searchKey = "search-menu";
    handleSearchButton().run();
    App.addEventChangePage(searchKey, handleSearchButton());

    // favorite button
    String favoriteKey = "favorite-menu";
    handleFavoriteButton().run();
    App.addEventChangePage(favoriteKey, handleFavoriteButton());

    // playlists button
    handlePlaylistsButton();

    // playlist box
    handlePlaylistBox().run();
    String playlistKey = "playlist-box-menu";
    AccountManager.addEventChangePlaylist(playlistKey, handlePlaylistBox());
    AccountManager.addEventLogin(playlistKey, handlePlaylistBox());

    // create playlist button
    Platform.runLater(() -> handleCreatePlaylistButton());
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

  private Runnable handleFavoriteButton() {
    favoriteButton.setOnAction(e -> {
      // check user login
      if (AccountManager.getId() < 0) {
        App.redirect(DefindUI.getLogin());
        return;
      }

      // check current page
      if (App.getCurrentContent() != DefindUI.getFavorite()) {
        App.redirect(DefindUI.getFavorite());
        return;
      }
    });

    return () -> {
      if (App.getCurrentContent() == DefindUI.getFavorite()) {
        favoriteButton.getStyleClass().add("selected");
      } else {
        favoriteButton.getStyleClass().remove("selected");
      }
    };
  }

  private void handlePlaylistsButton() {
    playlistsButton.setOnAction(e -> {
      // check user login
      if (AccountManager.getId() < 0) {
        App.redirect(DefindUI.getLogin());
        return;
      }
    });
  }

  private Runnable handlePlaylistBox() {
    return () -> {
      // clear playlist box
      playlistBox.getChildren().clear();

      // load playlist
      LinkedList<PlaylistModel> playlists = AccountManager.getPlaylists();

      if (playlists == null) {
        return;
      }

      // add playlist item
      for (int i = 0; i < playlists.size(); i++) {
        try {
          FXMLLoader loader = DefindUI.loadFXML(DefindUI.getPlaylistMenu());
          Parent playlistItem = loader.load();

          // set data
          PlaylistMenuController controller = loader.getController();
          controller.setData(playlists.get(i));

          playlistBox.add(playlistItem, 0, i);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
  }

  private void handleCreatePlaylistButton() {
    // handle create playlist
    CustomDialog createPlaylistDialog = new CustomDialog("Tạo danh sách phát");

    // load content
    try {
      FXMLLoader loader = DefindUI.loadFXML(DefindUI.getPlaylistCreate());
      StackPane stackPane = loader.load();

      PlaylistCreateController controller = loader.getController();
      controller.setDialog(createPlaylistDialog);

      // set content
      createPlaylistDialog.loadContent(stackPane);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // create playlist button
    createPlaylistButton.setOnAction(e -> {
      // check user login
      if (AccountManager.getId() < 0) {
        App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
        return;
      }

      // show dialog
      createPlaylistDialog.show();
    });
  }
}
