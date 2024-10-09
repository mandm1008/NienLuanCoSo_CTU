package ui.controllers;

import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import modules.AccountManager;
import modules.ImageManager;
import modules.LoadLater;
import modules.NotificationManager;
import modules.YoutubeData;
import ui.App;
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

  // for Youtube
  @FXML
  private TextField srcFieldYT;
  @FXML
  private Button checkLinkButtonYT;
  @FXML
  private TextField nameFieldYT;
  @FXML
  private TextField artistFieldYT;
  @FXML
  private ImageView checkImageViewYT;
  @FXML
  private Button uploadBtnYT;

  private boolean isUploaded = false;
  private YoutubeData youtubeData = null;

  public void initialize() {
    // local
    handleChooseSrc();
    handleChooseImage();
    handleCheckImage();
    handleChooseName();
    handleChooseArtist();
    handleUpload();

    // youtube
    uploadBtnYT.setDisable(true);
    handleCheckLinkYT();
    handleUploadYT();
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

        if (AccountManager.uploadSong(nameString, artistString, audioHref, imageHref)) {
          clearFields();
          App.getNotificationManager().notify("Upload thành công!", NotificationManager.SUCCESS);
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

  // for Youtube
  private void handleCheckLinkYT() {
    checkLinkButtonYT.setOnAction(e -> {
      uploadBtnYT.setDisable(true);
      if (isUploaded == false && youtubeData != null) {
        youtubeData.deleteUploadFile();
      }

      String link = srcFieldYT.getText();
      if (link.isEmpty()) {
        return;
      }

      // get video data
      new Thread(() -> {
        YoutubeData data = YoutubeData.download(link);
        if (data == null) {
          return;
        }
        if (!data.upload()) {
          App.getNotificationManager().notify("Upload thất bại!", NotificationManager.ERROR);
          return;
        }
        youtubeData = data;

        // set data
        Platform.runLater(() -> {
          nameFieldYT.setText(data.getTitle());
          artistFieldYT.setText(data.getChannelTitle());
        });

        // set thumbnail
        Callback<Image, Void> callback = new Callback<Image, Void>() {
          @Override
          public Void call(Image image) {
            Platform.runLater(() -> {
              checkImageViewYT.setImage(image);
              uploadBtnYT.setDisable(false);
            });
            return null;
          }
        };

        LoadLater.addLoader(data.getThumbnail(), callback);
      }).start();
    });
  }

  private void handleUploadYT() {
    uploadBtnYT.setOnAction(e -> {
      if (youtubeData == null) {
        App.getNotificationManager().notify("Chưa kiểm tra link!", NotificationManager.ERROR);
        return;
      }

      String title;
      String channelTitle = youtubeData.getChannelTitle();
      String thumbnail = youtubeData.getThumbnail();
      String href = youtubeData.getHref();

      if (nameFieldYT.getText().isEmpty()) {
        title = youtubeData.getTitle();
      } else {
        title = nameFieldYT.getText();
      }

      if (AccountManager.uploadSong(title, channelTitle, href, thumbnail)) {
        clearFieldsYT();
        App.getNotificationManager().notify("Upload thành công!", NotificationManager.SUCCESS);
        isUploaded = true;
      } else {
        App.getNotificationManager().notify("Upload thất bại!", NotificationManager.ERROR);
      }

    });
  }

  private void clearFieldsYT() {
    srcFieldYT.clear();
    nameFieldYT.clear();
    artistFieldYT.clear();
    checkImageViewYT.setImage(ImageManager.getImage(ImageManager.DEMO_MUSIC));
    isUploaded = false;
    youtubeData = null;
  }

}
