package ui.controllers;

import java.io.IOException;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
// import javafx.geometry.Insets;
// import javafx.scene.control.Button;
import javafx.scene.control.Label;
// import javafx.scene.control.Tooltip;
// import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
// import javafx.scene.layout.HBox;
import modules.AccountManager;
// import modules.ImageManager;
// import modules.NotificationManager;
// import ui.App;
import ui.DefindUI;
// import db.PlaylistModel;
import db.SongModel;

public class UserPageController {
  private static final int NULL_TARGET = -1;
  public static int TARGET = NULL_TARGET;

  public static void setTarget(int target) {
    TARGET = target;
  }

  public static void resetTarget() {
    TARGET = NULL_TARGET;
  }

  // @FXML
  // private Label myPlaylistTitle;
  // @FXML
  // private GridPane myPlaylistGridPane;
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
        // loadMyPlaylist();
        loadMySong();
        loadLiked();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();

    // // events
    // AccountManager.addEventChangePlaylist("user-page-playlist", () -> {
    // loadMyPlaylist();
    // });
  }

  // public void loadMyPlaylist() {
  // // clear all children
  // myPlaylistGridPane.getChildren().clear();

  // // load data
  // LinkedList<PlaylistModel> playlists;
  // if (TARGET == NULL_TARGET) {
  // playlists = AccountManager.getPlaylists();
  // } else {
  // PlaylistModel pm = new PlaylistModel();
  // playlists = pm.findByUserId(TARGET);
  // }

  // Platform.runLater(() -> {
  // // load title
  // myPlaylistTitle.setText("Danh sách phát (" + playlists.size() + ")");

  // // load songs
  // for (int i = 0; i < playlists.size(); i++) {
  // PlaylistModel pl = playlists.get(i);

  // HBox box = new HBox();
  // box.getStylesheets().add(UserPageController.class.getResource("/css/music.css").toExternalForm());
  // box.getStyleClass().add("music-box");
  // box.setAlignment(javafx.geometry.Pos.CENTER);
  // box.setPadding(new Insets(10));
  // box.setSpacing(10);
  // myPlaylistGridPane.add(box, i % 4, (int) i / 4);

  // Label title = new Label(pl.getName());
  // title.getStyleClass().add("music-title");

  // // edit btn
  // Button editBt = new Button();
  // editBt.getStyleClass().add("clear-button");
  // ImageView editIcon = new ImageView(ImageManager.getImage(ImageManager.EDIT));
  // editIcon.setFitHeight(20);
  // editBt.setGraphic(editIcon);
  // Tooltip.install(editBt, new Tooltip("Chỉnh sửa"));

  // // copy btn
  // Button copyBt = new Button();
  // copyBt.getStyleClass().add("clear-button");
  // ImageView copyIcon = new ImageView(ImageManager.getImage(ImageManager.COPY));
  // copyIcon.setFitHeight(20);
  // copyBt.setGraphic(copyIcon);
  // Tooltip.install(copyBt, new Tooltip("Sao chép"));
  // copyBt.setOnAction((e) -> {
  // if (AccountManager.copyPlaylist(pl)) {
  // App.getNotificationManager().notify("Sao chép '" + pl.getName() + "' thành
  // công!",
  // NotificationManager.SUCCESS);
  // loadMyPlaylist();
  // } else {
  // App.getNotificationManager().notify("Sao chép '" + pl.getName() + "' thất
  // bại! Vui lòng thử lại",
  // NotificationManager.ERROR);
  // }
  // });

  // // list btn
  // Button listBt = new Button();
  // listBt.getStyleClass().add("clear-button");
  // ImageView listIcon = new ImageView(ImageManager.getImage(ImageManager.MENU));
  // listIcon.setFitHeight(20);
  // listIcon.setFitWidth(20);
  // listBt.setGraphic(listIcon);
  // Tooltip.install(listBt, new Tooltip("Danh sách nhạc"));

  // box.getChildren().addAll(title, editBt, copyBt, listBt);
  // HBox.setHgrow(title, javafx.scene.layout.Priority.ALWAYS);

  // if (TARGET == NULL_TARGET) {

  // } else {
  // editBt.setVisible(false);
  // editBt.setDisable(true);
  // }

  // }
  // });
  // }

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
