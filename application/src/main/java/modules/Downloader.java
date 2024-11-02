package modules;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import db.SongModel;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;

import ui.App;

public class Downloader {
  private static final String DEFAULT_SAVE_PATH = "./musics";
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

    // check savePath
    if (savePath == null || savePath.isEmpty()) {
      savePath = DEFAULT_SAVE_PATH;
    }

    // check valid path
    if (!Files.exists(Paths.get(savePath))) {
      try {
        Files.createDirectories(Paths.get(savePath));
      } catch (IOException e) {
        e.printStackTrace();
        // notify
        Platform.runLater(() -> {
          App.getNotificationManager().notify("Tải nhạc thất bại: " + fileName, NotificationManager.ERROR);
        });
        return;
      }
    }

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

  public static void download(String downloadUrl, String fileName) {
    Downloader downloader = new Downloader(downloadUrl, DEFAULT_SAVE_PATH, fileName);
    downloader.download();
  }

  public static void download(String driveDownloadUrl) throws Exception {
    Downloader downloader = new Downloader(driveDownloadUrl, DEFAULT_SAVE_PATH,
        FileService.getFileName(driveDownloadUrl));
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

  public static void run(String downloadUrl) {
    Platform.runLater(() -> {
      try {
        String fileName = FileService.getFileName(downloadUrl);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Chọn thư mục lưu nhạc: " + fileName);

        String savePath = directoryChooser.showDialog(App.getPrimaryStage()).getAbsolutePath();
        Downloader.download(downloadUrl, savePath, fileName);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  public static Path getStoreDir() {
    Path storeDir = Paths.get(DEFAULT_SAVE_PATH);

    File storeDirFile = storeDir.toFile();
    if (!storeDirFile.exists()) {
      storeDirFile.mkdirs();
    }

    return storeDir;
  }

  public static LinkedList<SongModel> getDownloadedSongs() {
    LinkedList<SongModel> songs = new LinkedList<SongModel>();

    try {
      Files.list(getStoreDir()).forEach(path -> {
        String fileName = path.getFileName().toString();
        String[] parts = fileName.split("\\.");

        if (parts.length < 2) {
          return;
        }

        String extension = parts[parts.length - 1];
        if (!extension.equals("mp3") && !extension.equals("mp4")) {
          return;
        }

        String imageUri = Downloader.class.getResource("/images/demo_music.png").toExternalForm();

        try (DirectoryStream<Path> imageStream = Files.newDirectoryStream(getStoreDir())) {
          for (Path imagePath : imageStream) {
            String imageName = imagePath.getFileName().toString();
            if ((imageName.endsWith(".jpg") || imageName.endsWith(".png")) && imageName.startsWith(parts[0])) {
              imageUri = imagePath.toUri().toString();
              break;
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }

        SongModel song = new SongModel(fileName, path.toUri().toString(), imageUri);
        songs.add(song);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    return songs;
  }
}
