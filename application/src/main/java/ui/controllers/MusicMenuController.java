package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

import modules.AccountManager;
import modules.Downloader;
import modules.ImageManager;
import modules.NotificationManager;
import ui.App;
import db.SongModel;

public class MusicMenuController {
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

  private void handleDownloadButton() {
    downloadButton.setOnAction(e -> {
      System.out.println("Download: " + songData.getHref());
      Downloader.run(songData.getHref(), songData.getTitle() + ".mp3");
    });
  }

  @SuppressWarnings("exports")
  public void setSong(SongModel song) {
    songData = song;
    title.setText("-- " + song.getTitle() + " --");
  }
}
