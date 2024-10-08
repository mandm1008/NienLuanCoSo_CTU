package modules;

import java.nio.file.Path;
import java.nio.file.Paths;

import db.FileService;

public class YoutubeData {
  private String title;
  private String channelTitle;
  private String thumbnail;
  private String href;

  public YoutubeData() {
    this.title = "";
    this.channelTitle = "";
    this.thumbnail = "";
    this.href = "";
  }

  public YoutubeData(String title, String channelTitle, String thumbnail) {
    this.title = title;
    this.channelTitle = channelTitle;
    this.thumbnail = thumbnail;
  }

  public YoutubeData(YoutubeData data) {
    this.title = data.title;
    this.channelTitle = data.channelTitle;
    this.thumbnail = data.thumbnail;
    this.href = data.href;
  }

  public String getTitle() {
    return title;
  }

  public String getChannelTitle() {
    return channelTitle;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public String getHref() {
    return href;
  }

  public static String uploadMp4(String title) {
    Path path = Paths.get(System.getProperty("user.home"), "Downloads");
    Path file = path.resolve(YoutubeDownloader.sanitizeFileName(title) + ".mp4");

    try {
      return FileService.uploadFile(file.toAbsolutePath().toString(), "video/mp4", title);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String uploadThumbnail(String title) {
    Path path = Paths.get(System.getProperty("user.home"), "Downloads");
    Path file = path.resolve(YoutubeDownloader.sanitizeFileName(title) + ".jpg");

    try {
      return FileService.uploadFile(file.toAbsolutePath().toString(), "image/jpeg", title);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static YoutubeData loadData(String youtubeUrl) {
    YoutubeData data = YoutubeDownloader.getYouTubeInfo(youtubeUrl);

    // Download video
    YoutubeDownloader.downloadVideo(youtubeUrl, data.title);

    // Download thumbnail
    YoutubeDownloader.downloadThumbnail(data.thumbnail, data.title);

    data.href = uploadMp4(data.title);
    data.thumbnail = uploadThumbnail(data.title);

    // clean data
    YoutubeDownloader.cleanVideo(data.title);
    YoutubeDownloader.cleanThumbnail(data.title);

    return data;
  }

  public static void main(String[] args) {
    try {
      YoutubeData data = loadData("https://www.youtube.com/watch?v=qZ9DaB7xRZs");
      System.out.println("Title: " + data.title);
      System.out.println("Channel: " + data.channelTitle);
      System.out.println("Thumbnail: " + data.thumbnail);
      System.out.println("Href: " + data.href);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
