package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import modules.Downloader;
import modules.NotificationManager;
import db.SongModel;
import ui.App;

public class MusicUserMenuController extends MenuMusic {
  @FXML
  private Label title;
  @FXML
  private Button editButton;
  @FXML
  private Button saveDeleteButton;
  @FXML
  private Button downloadButton;

  private SongModel songData = new SongModel();

  public void initialize() {
    // edit button
    handleEditButton();

    // save/delete button
    handleSaveDeleteButton();

    // download button
    handleDownloadButton();
  }

  private void handleEditButton() {
    editButton.setOnAction(e -> {
      System.out.println("Edit: " + songData.getTitle());
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

  @SuppressWarnings("exports")
  @Override
  public void setSong(SongModel song) {
    songData = song;
    title.setText("-- " + song.getTitle() + " --");
  }
}
