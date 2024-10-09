package modules;

import db.FileService;
import ui.App;

public class YoutubeData {
  private String title;
  private String channelTitle;
  private String thumbnail;
  private String thumbnailLocal = null;
  private String href;
  private String hrefLocal = null;

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

  public String getThumbnailLocal() {
    return thumbnailLocal;
  }

  private void setThumbnailLocal(String thumbnailLocal) {
    this.thumbnailLocal = thumbnailLocal;
  }

  private void setHrefLocal(String hrefLocal) {
    this.hrefLocal = hrefLocal;
  }

  public String getHrefLocal() {
    return hrefLocal;
  }

  public static String uploadMp4(YoutubeData data) {
    try {
      return FileService.uploadFile(data.hrefLocal, "video/mp4", data.title + ".mp4");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String uploadThumbnail(YoutubeData data) {
    try {
      return FileService.uploadFile(data.thumbnailLocal, "image/jpeg", data.title + ".jpg");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static YoutubeData download(String youtubeUrl) {
    YoutubeData data = YoutubeDownloader.getYouTubeInfo(youtubeUrl);

    if (data == null) {
      App.getNotificationManager().notify("Link không hợp lệ!", NotificationManager.ERROR);
      return null;
    }

    // Download video
    if (data.hrefLocal == null) {
      String hrefLocal = YoutubeDownloader.downloadVideo(youtubeUrl, data.title);
      if (hrefLocal == null) {
        App.getNotificationManager().notify("Lỗi khi tải video!", NotificationManager.ERROR);
        return null;
      }
      data.setHrefLocal(hrefLocal);
    }

    // Download thumbnail
    if (data.thumbnailLocal == null) {
      String thumbnailLocal = YoutubeDownloader.downloadThumbnail(data.thumbnail, data.title);
      if (thumbnailLocal == null) {
        App.getNotificationManager().notify("Lỗi khi tải ảnh!", NotificationManager.ERROR);
        return null;
      }
      data.setThumbnailLocal(thumbnailLocal);
    }

    return data;
  }

  public boolean upload() {
    this.href = uploadMp4(this);
    this.thumbnail = uploadThumbnail(this);

    // check upload
    if (this.href == null || this.thumbnail == null) {
      return false;
    }

    // clean data
    YoutubeDownloader.cleanVideo(this);
    YoutubeDownloader.cleanThumbnail(this);

    return true;
  }

  public boolean deleteUploadFile() {
    try {
      FileService.delete(
          this.href.substring(this.href.lastIndexOf("?export=download&id=") + ("?export=download&id=").length()));
      FileService.delete(
          this.thumbnail
              .substring(this.thumbnail.lastIndexOf("?export=download&id=") + ("?export=download&id=").length()));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static void main(String[] args) {
    try {
      YoutubeData data = download("https://www.youtube.com/watch?v=qzrv-g06yhU");
      data.upload();
      System.out.println("Title: " + data.title);
      System.out.println("Channel: " + data.channelTitle);
      System.out.println("Thumbnail: " + data.thumbnail);
      System.out.println("Href: " + data.href);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
