package ui.controllers;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import modules.AccountManager;
import modules.ImageManager;
import modules.NotificationManager;

import ui.App;
import db.SongModel;
import db.ArtistModel;
import db.FileService;

public class UploadController {
  @FXML
  private TextField srcField;
  @FXML
  private Button chooseSrcBtn;
  @FXML
  private TextField imageField;
  @FXML
  private Button chooseImageBtn;
  @FXML
  private Button checkImageBtn;
  @FXML
  private ImageView checkImageView;
  @FXML
  private TextField nameField;
  @FXML
  private Button chooseNameBtn;
  @FXML
  private TextField artistField;
  @FXML
  private Button chooseArtistBtn;
  @FXML
  private Button uploadBtn;

  public void initialize() {
    handleChooseSrc();
    handleChooseImage();
    handleCheckImage();
    handleChooseName();
    handleChooseArtist();
    handleUpload();
  }

  private void handleChooseSrc() {
    chooseSrcBtn.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Chọn file nhạc");

      File file = fileChooser.showOpenDialog(App.getPrimaryStage());

      if (file != null) {
        srcField.setText(file.getAbsolutePath());
      }
    });
  }

  private void handleChooseImage() {
    chooseImageBtn.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Chọn file ảnh");

      File file = fileChooser.showOpenDialog(App.getPrimaryStage());

      if (file != null) {
        imageField.setText(file.getAbsolutePath());
      }
    });
  }

  private void handleCheckImage() {
    checkImageBtn.setOnAction(e -> {
      String path = imageField.getText();
      if (path.isEmpty()) {
        return;
      }

      Image image = new Image(new File(path).toURI().toString());

      if (image.isError()) {
        App.getNotificationManager().notify("Đường dẫn không chính xác!", NotificationManager.ERROR);
        return;
      }

      checkImageView.setImage(image);
    });
  }

  private void handleChooseName() {
    chooseNameBtn.setOnAction(e -> {

    });
  }

  private void handleChooseArtist() {
    chooseArtistBtn.setOnAction(e -> {

    });
  }

  private void handleUpload() {
    uploadBtn.setOnAction(e -> {
      String fileSRCString = srcField.getText();
      String imageSRCString = imageField.getText();
      String nameString = nameField.getText();
      String artistString = artistField.getText();

      String audioHref;
      String imageHref;

      try {
        // upload .mp3 file
        audioHref = FileService.uploadFile(fileSRCString, "audio/mpeg", nameString);
        // upload image file
        imageHref = FileService.uploadFile(imageSRCString, "image/png", nameString + "_image");

        // save artist
        ArtistModel artist = new ArtistModel(artistString);
        // check if artist exists
        artist.findByName();
        if (artist.getArtistId() <= 0) {
          artist.insert();
          artist.findByName();
        }

        // get user id
        int userId = AccountManager.getId();
        if (userId <= 0) {
          userId = 0; // unknown user
        }

        // save song
        SongModel song = new SongModel(nameString, userId, artist.getArtistId(), audioHref, imageHref);
        song.insert();
        song.findByTitleAndHref();

        if (song.getSongId() >= 0) {
          System.out.println("Upload success!");
          App.getNotificationManager().notify("Upload thành công!", NotificationManager.SUCCESS);

          // clear fields
          clearFields();
        } else {
          App.getNotificationManager().notify("Upload thất bại!", NotificationManager.ERROR);
        }
      } catch (Exception err) {
        err.printStackTrace();
      }
    });
  }

  private void clearFields() {
    srcField.clear();
    imageField.clear();
    nameField.clear();
    artistField.clear();
    checkImageView.setImage(ImageManager.getImage(ImageManager.DEMO_MUSIC));
  }

}
