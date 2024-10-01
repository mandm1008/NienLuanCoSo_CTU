package modules;

import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.stage.DirectoryChooser;

import ui.App;

public class Downloader {
  private String downloadUrl;
  private String savePath;
  private String fileName;

  public Downloader(String downloadUrl, String savePath, String fileName) {
    this.downloadUrl = new String(downloadUrl);
    this.savePath = new String(savePath);
    this.fileName = new String(fileName);
  }

  public void download() {
    // download file
    URI uri = URI.create(downloadUrl);
    URL url;
    try {
      url = uri.toURL();
      InputStream in = url.openStream();

      // save file
      Files.copy(in, Paths.get(savePath, fileName));

      // notify
      Platform.runLater(() -> {
        App.getNotificationManager().notify("Tải nhạc thành công: " + fileName, NotificationManager.SUCCESS);
      });
    } catch (MalformedURLException e) {
      e.printStackTrace();
      // notify
      Platform.runLater(() -> {
        App.getNotificationManager().notify("Tải nhạc thất bại: " + fileName, NotificationManager.ERROR);
      });
    } catch (IOException e) {
      e.printStackTrace();
      // notify
      Platform.runLater(() -> {
        App.getNotificationManager().notify("Tải nhạc thất bại: " + fileName, NotificationManager.ERROR);
      });
    }
  }

  public static void download(String downloadUrl, String savePath, String fileName) {
    Downloader downloader = new Downloader(downloadUrl, savePath, fileName);
    downloader.download();
  }

  public static void run(String downloadUrl, String fileName) {
    Platform.runLater(() -> {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Chọn thư mục lưu nhạc: " + fileName);

      try {
        String savePath = directoryChooser.showDialog(App.getPrimaryStage()).getAbsolutePath();
        Downloader.download(downloadUrl, savePath, fileName);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
