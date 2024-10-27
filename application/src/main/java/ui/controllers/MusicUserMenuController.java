package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import modules.AccountManager;
import modules.Downloader;
import modules.ImageManager;
import modules.NotificationManager;
import db.SongModel;
import ui.App;

public class MusicUserMenuController extends MenuMusic {
  @FXML
  private Label title;
  @FXML
  private Button likeButton;
  @FXML
  private ImageView likeImage;
  @FXML
  private Button playNowButton;
  @FXML
  private Button addPlaylistButton;
  @FXML
  private Button shareButton;
  @FXML
  private Button editButton;
  @FXML
  private Button saveDeleteButton;
  @FXML
  private Button downloadButton;

  private SongModel songData = new SongModel();

  public void initialize() {
    // load like
    handleLikeButton().run();

    // play now button
    handlePlayNowButton();

    // add playlist button
    handleAddPlaylistButton();

    // share button
    handleShareButton();

    // edit button
    handleEditButton();

    // save/delete button
    handleSaveDeleteButton();

    // download button
    handleDownloadButton();
  }

  private Runnable handleLikeButton() {
    // check login
    if (AccountManager.getId() < 0) {
      likeImage.setImage(ImageManager.getImage(ImageManager.LIKED));
      likeButton.setText("    Thích");
    }

    // like button
    likeButton.setOnAction(e -> {
      if (AccountManager.checkLikedSong(songData.getSongId())) {
        AccountManager.unlikeSong(songData.getSongId());
        likeImage.setImage(ImageManager.getImage(ImageManager.LIKED));
        likeButton.setText("    Thích");
      } else {
        AccountManager.likeSong(songData.getSongId());
        likeImage.setImage(ImageManager.getImage(ImageManager.LIKE));
        likeButton.setText("    Bỏ thích");
      }
    });

    return () -> {
      if (!AccountManager.checkLikedSong(songData.getSongId())) {
        likeImage.setImage(ImageManager.getImage(ImageManager.LIKED));
        likeButton.setText("    Thích");
      } else {
        likeImage.setImage(ImageManager.getImage(ImageManager.LIKE));
        likeButton.setText("    Bỏ thích");
      }
    };
  }

  private void handlePlayNowButton() {
    playNowButton.setOnAction(e -> {
      App.getMusicManager().changeMusic(songData);
    });
  }

  private void handleAddPlaylistButton() {
    addPlaylistButton.setOnAction(e -> {
      double x = addPlaylistButton.localToScreen(addPlaylistButton.getBoundsInLocal()).getMaxX();
      double y = addPlaylistButton.localToScreen(addPlaylistButton.getBoundsInLocal()).getMinY();
      AccountManager.addToPlaylist(songData, x, y);
    });
  }

  private void handleShareButton() {
    shareButton.setOnAction(e -> {
      System.out.println("Share: " + songData.getHref());
      App.getNotificationManager().notify("Share: " + songData.getTitle(), NotificationManager.SUCCESS);
    });
  }

  private void handleEditButton() {
    Popup popup = new Popup();
    popup.setAutoHide(true);

    // set content
    VBox box = new VBox();
    box.setStyle("-fx-border-color: white;" +
        "fx-border-width: 2px;" +
        "fx-border-radius: 5px;" +
        "fx-background-color: #664E88;" +
        "fx-background-radius: 5px;" +
        " -fx-padding: 10px;");
    box.getStylesheets().add(MusicUserMenuController.class.getResource("/css/upload.css").toExternalForm());
    box.setSpacing(8);
    box.setFillWidth(true);
    box.setAlignment(javafx.geometry.Pos.CENTER);

    // add content
    TextField nameField = new TextField();
    nameField.setPromptText("Đổi tên bài hát");
    nameField.getStyleClass().add("upload-item--field");
    nameField.setPrefWidth(200);
    nameField.setPrefHeight(32);

    HBox buttonBox = new HBox();
    buttonBox.setSpacing(16);
    buttonBox.setFillHeight(true);
    buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

    Button saveButton = new Button("Lưu");
    saveButton.getStyleClass().add("upload-item--button");
    saveButton.setPrefWidth(80);
    saveButton.setPrefHeight(32);
    saveButton.setDisable(true);

    Button cancelButton = new Button("Hủy");
    cancelButton.getStyleClass().add("upload-item--button");
    cancelButton.setPrefWidth(80);
    cancelButton.setPrefHeight(32);

    // add to button box
    buttonBox.getChildren().addAll(saveButton, cancelButton);

    // events
    nameField.textProperty().addListener((observable, oldValue, newValue) -> {
      saveButton.setDisable(songData.getTitle().equals(newValue));
    });
    saveButton.setOnAction(e -> {
      System.out.println("Save: " + songData.getTitle());
      if (songData.updateTitle(nameField.getText())) {
        App.getNotificationManager().notify("Đổi tên bài hát thành công", NotificationManager.SUCCESS);
      } else {
        App.getNotificationManager().notify("Đổi tên bài hát thất bại! Vui lòng thử lại", NotificationManager.ERROR);
      }
      popup.hide();
    });
    cancelButton.setOnAction(e -> {
      System.out.println("Cancel: " + songData.getTitle());
      popup.hide();
    });

    // add to box
    box.getChildren().addAll(nameField, buttonBox);

    // add to popup
    popup.getContent().add(box);

    editButton.setOnAction(e -> {
      System.out.println("Edit: " + songData.getTitle());
      nameField.setText(songData.getTitle());
      // get position
      double x = editButton.localToScreen(editButton.getBoundsInLocal()).getMaxX();
      double y = editButton.localToScreen(editButton.getBoundsInLocal()).getMinY();

      // set position
      popup.setX(x);
      popup.setY(y);

      // show popup
      popup.show(App.getPrimaryStage());
    });
  }

  private void handleSaveDeleteButton() {
    saveDeleteButton.setOnAction(e -> {
      System.out.println("Save/Delete: " + songData.getTitle());
      Downloader.run(songData.getHref(), songData.getTitle() + ".mp3");

      // delete
      if (songData.delete()) {
        App.getNotificationManager().notify("Xóa bài hát thành công", NotificationManager.SUCCESS);
      } else {
        App.getNotificationManager().notify("Xóa bài hát thất bại", NotificationManager.ERROR);
      }
    });
  }

  private void handleDownloadButton() {
    downloadButton.setOnAction(e -> {
      System.out.println("Download: " + songData.getHref());
      Downloader.run(songData.getHref(), songData.getTitle() + ".mp3");
    });
  }

  // @SuppressWarnings("exports")
  @Override
  public void setSong(SongModel song) {
    songData = song;
    title.setText("-- " + song.getTitle() + " --");
  }
}
