package ui.controllers;

import java.io.IOException;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import modules.AccountManager;
import modules.CustomDialog;
import modules.ImageManager;
import modules.NotificationManager;
import modules.SearchManager;
import modules.YoutubeData;
import ui.App;
import ui.DefindUI;
import db.PlaylistModel;
import db.PlaylistSongModel;
import db.SongModel;

public class PlaylistCreateController {
  @FXML
  private TextField nameField;
  @FXML
  private Button createButton;
  @FXML
  private Button cancelButton;
  @FXML
  private ChoiceBox<String> choiceBox;
  @FXML
  private Button deleteButton;
  @FXML
  private GridPane currentPlaylistGridPane;
  @FXML
  private TextField searchTextField;
  @FXML
  private GridPane searchGridPane;
  @FXML
  private GridPane userListGridPane;
  @FXML
  private TextField youtubeTextField;
  @FXML
  private Button youtubeButton;

  private CustomDialog dialog;
  private PlaylistModel choicePlaylistModel;
  private SearchManager searchManager;
  LinkedList<SongModel> songsChoice = new LinkedList<>();

  public void initialize() {
    // init search manager
    searchManager = new SearchManager();
    searchManager.addEventOnSearch("playlist-create-search", () -> {
      Platform.runLater(handleSearch());
    });
    handleSearchField();

    // choice box
    Platform.runLater(handleChoiceBox());
    AccountManager.addEventLogin("playlist-create-current", handleChoiceBox());
    AccountManager.addEventLogout("playlist-create-current", handleChoiceBox());
    AccountManager.addEventChangePlaylist("playlist-create-current", handleChoiceBox());

    // create button
    createButton.setOnAction(e -> {
      String name = nameField.getText();
      if (name.isEmpty()) {
        return;
      }

      // create playlist
      if (AccountManager.createPlaylist(name)) {
        nameField.clear();
        // notify
        dialog.getNotificationManager().notify("Tạo danh sách thành công!", NotificationManager.SUCCESS);
      } else {
        dialog.getNotificationManager().notify("Bạn đã có danh sách tên này! Thử lại với tên khác!",
            NotificationManager.ERROR);
      }
    });

    // cancel button
    cancelButton.setOnAction(e -> {
      nameField.clear();
    });

    // delete button
    deleteButton.setOnAction(e -> {
      if (choicePlaylistModel == null) {
        return;
      }

      if (AccountManager.removePlaylist(choicePlaylistModel)) {
        dialog.getNotificationManager().notify("Xóa danh sách thành công!", NotificationManager.SUCCESS);
      } else {
        dialog.getNotificationManager().notify("Xóa danh sách thất bại!", NotificationManager.ERROR);
      }
    });

    // user
    AccountManager.addEventLogin("playlist-create-user", () -> {
      Platform.runLater(handleUser());
    });
    AccountManager.addEventUpload("playlist-create-user", () -> {
      Platform.runLater(handleUser());
    });
    handleUser().run();

    // youtube
    youtubeButton.setOnAction(e -> {
      youtubeButton.setDisable(true);

      new Thread(() -> {
        if (handleYoutube()) {
          Platform.runLater(() -> {
            youtubeTextField.clear();
          });
        }

        Platform.runLater(() -> {
          youtubeButton.setDisable(false);
        });
      }).start();
    });
  }

  // @SuppressWarnings("exports")
  public void setDialog(CustomDialog dialog) {
    this.dialog = dialog;
  }

  private boolean isOnPlaylist(LinkedList<PlaylistModel> playlists, String name) {
    return playlists.stream().anyMatch(playlist -> playlist.getName().equals(name));
  }

  private Runnable handleChoiceBox() {
    return () -> {
      // clear action
      choiceBox.setOnAction(null);

      LinkedList<PlaylistModel> playlists = AccountManager.getPlaylists();
      String oldValue = null;

      if (playlists == null) {
        choiceBox.getItems().clear();
        return;
      }

      System.out.println("Playlist: " + playlists.size());

      // check choice box
      currentPlaylistGridPane.getChildren().clear();
      if (choiceBox.getValue() != null && isOnPlaylist(playlists, choiceBox.getValue())) {
        // load current music
        handleLoadCurrentMusic(playlists);
        System.out.println("Load current music");
        oldValue = choiceBox.getValue();
      }

      // load data
      choiceBox.getItems().clear();

      playlists.forEach(playlist -> {
        choiceBox.getItems().add(playlist.getName());
      });

      // set value
      if (oldValue != null) {
        choiceBox.setValue(oldValue);
      }

      // event
      choiceBox.setOnAction(e -> {
        System.out.println("Run set on action choice box");
        handleLoadCurrentMusic(playlists);
      });
    };
  }

  private void handleLoadCurrentMusic() {
    LinkedList<PlaylistModel> playlists = AccountManager.getPlaylists();
    handleLoadCurrentMusic(playlists);
  }

  private void handleLoadCurrentMusic(LinkedList<PlaylistModel> playlists) {
    String name = choiceBox.getValue();
    if (name == null) {
      currentPlaylistGridPane.getChildren().clear();
      return;
    }

    // get playlist
    choicePlaylistModel = playlists.stream().filter(playlist -> playlist.getName().equals(name)).findFirst().get();
    PlaylistSongModel psm = new PlaylistSongModel(choicePlaylistModel.getPlaylistId());

    // show playlist
    currentPlaylistGridPane.getChildren().clear();
    LinkedList<SongModel> songs = psm.getSongsByPlaylistId(choicePlaylistModel.getPlaylistId());
    songsChoice = songs;

    for (int index = 0; index < songs.size(); index++) {
      SongModel song = songs.get(index);

      Platform.runLater(() -> {
        FXMLLoader loader;
        try {
          loader = DefindUI.loadFXML(DefindUI.getPlaylistItem());
          currentPlaylistGridPane.add(loader.load(), 0, currentPlaylistGridPane.getChildren().size());

          // controller
          PlaylistItemController controller = loader.getController();
          controller.setTitle(song.getTitle());
          if (song.getArtistId() <= 0) {
            controller.setArtist(song.getArtistName());
          } else {
            controller.setArtist(song.getArtist().getName());
          }
          controller.setImage(song.getImage());
          controller.setStyleBox("-fx-background-color: #664E88; -fx-background-radius: 5px; -fx-padding: 10px;");
          controller.setIndex(-999);
          controller.removePlayBtn();

          controller.setActionRemoveBtn(() -> {
            // remove song
            PlaylistSongModel psm2 = new PlaylistSongModel(choicePlaylistModel.getPlaylistId(), song.getSongId());
            psm2.getDataBySongIdAndPlaylistId();
            if (psm2.delete()) {
              dialog.getNotificationManager().notify("Xóa " + song.getTitle() + " thành công!",
                  NotificationManager.SUCCESS);
            } else {
              dialog.getNotificationManager().notify("Xóa " + song.getTitle() + " thất bại!",
                  NotificationManager.ERROR);
            }

            // reload
            Platform.runLater(() -> {
              handleLoadCurrentMusic();
            });
          });
        } catch (IOException err) {
          err.printStackTrace();
        }

      });
    }

    // reload search
    new Thread(() -> {
      Platform.runLater(handleSearch());
    }).start();

    // reload user
    new Thread(() -> {
      Platform.runLater(handleUser());
    }).start();
  }

  private void handleSearchField() {
    searchTextField.setOnKeyPressed(e -> {
      if (e.getCode() == SearchManager.KEY_CODE) {
        System.out.println("Search: " + searchTextField.getText());
        searchManager.search(searchTextField.getText(), true);
      }
    });
  }

  private Runnable handleSearch() {
    LinkedList<SongModel> songs = searchManager.getSongs();

    if (songs == null) {
      return () -> {
        searchGridPane.getChildren().clear();
      };
    }

    // filter songs
    songs.removeIf(song -> songsChoice.stream().anyMatch(s -> s.getSongId() == song.getSongId()));

    return handleLoadMusic(songs, searchGridPane);
  }

  private Runnable handleUser() {
    LinkedList<SongModel> songs = AccountManager.getSongs();

    if (songs == null) {
      return () -> {
        userListGridPane.getChildren().clear();
      };
    }

    // filter songs
    songs.removeIf(song -> songsChoice.stream().anyMatch(s -> s.getSongId() == song.getSongId()));

    return handleLoadMusic(songs, userListGridPane);
  }

  private Runnable handleLoadMusic(LinkedList<SongModel> songs, GridPane gridPane) {
    return () -> {
      // show search
      gridPane.getChildren().clear();
      for (int index = 0; index < songs.size(); index++) {
        SongModel song = songs.get(index);

        Platform.runLater(() -> {
          FXMLLoader loader;
          int i = gridPane.getChildren().size();
          int column = i % 2;
          int row = (int) (i / 2);

          try {
            loader = DefindUI.loadFXML(DefindUI.getPlaylistItem());
            gridPane.add(loader.load(), column, row);

            // controller
            PlaylistItemController controller = loader.getController();
            controller.setTitle(song.getTitle());
            controller.setArtist(song.getArtist().getName());
            controller.setImage(song.getImage());
            controller.setStyleBox("-fx-background-color: #664E88; -fx-background-radius: 5px; -fx-padding: 10px;");
            controller.setImageRemoveButton(ImageManager.getImage(ImageManager.PLAY_ADD));
            controller.setActionRemoveBtn(() -> {
              // add song
              PlaylistSongModel psm = new PlaylistSongModel(choicePlaylistModel.getPlaylistId(), song.getSongId());
              if (psm.insert()) {
                dialog.getNotificationManager().notify("Thêm " + song.getTitle() + " thành công!",
                    NotificationManager.SUCCESS);
              } else {
                dialog.getNotificationManager().notify("Thêm " + song.getTitle() + " thất bại!",
                    NotificationManager.ERROR);
              }

              Platform.runLater(() -> {
                handleLoadCurrentMusic();
              });
            });
            controller.removePlayBtn();
          } catch (IOException err) {
            err.printStackTrace();
          }

        });
      }
    };
  }

  private boolean handleYoutube() {
    String url = youtubeTextField.getText();
    if (url.isEmpty()) {
      return false;
    }

    // https://www.youtube.com/watch?v=xWWkx7oJylI
    if (!url.contains("https://www.youtube.com/watch?v=")) {
      App.getNotificationManager().notify("Link không hợp lệ!", NotificationManager.ERROR);
    }

    // remake url
    if (url.contains("&")) {
      url = url.substring(0, url.indexOf("&"));
    }

    YoutubeData ytData = YoutubeData.download(url);

    if (ytData == null) {
      return false;
    }

    // link-playlist-song
    PlaylistSongModel psm = new PlaylistSongModel(choicePlaylistModel.getPlaylistId(), url);
    if (psm.insertExternal()) {
      dialog.getNotificationManager().notify("Thêm " + ytData.getTitle() + " thành công!", NotificationManager.SUCCESS);
    } else {
      dialog.getNotificationManager().notify("Thêm " + ytData.getTitle() + " thất bại!", NotificationManager.ERROR);
      return false;
    }

    Platform.runLater(() -> {
      handleLoadCurrentMusic();
    });

    return true;
  }
}
