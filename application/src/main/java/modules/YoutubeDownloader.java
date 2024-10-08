package modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class YoutubeDownloader {
  public static String getOS() {
    String os = System.getProperty("os.name").toLowerCase();
    if (os.contains("win")) {
      return "windows";
    } else if (os.contains("nix") || os.contains("nux")) {
      return "linux";
    } else if (os.contains("mac")) {
      return "mac";
    }
    return "unknown";
  }

  public static File extractYTDownloader() throws IOException {
    String os = getOS();
    String resourcePath = "";

    if (os.equals("windows")) {
      resourcePath = "/bin/yt-dlp.exe";
    } else if (os.equals("linux")) {
      resourcePath = "/bin/yt-dlp-linux";
    } else if (os.equals("mac")) {
      resourcePath = "/bin/yt-dlp-mac";
    } else {
      throw new UnsupportedOperationException("Unsupported operating system: " + os);
    }

    Path tempPath = Files.createTempFile("yt-dlp", (os.equals("windows") ? ".exe" : ""));
    File tempFile = tempPath.toFile();

    try (InputStream inputStream = YoutubeDownloader.class.getResourceAsStream(resourcePath);
        OutputStream outputStream = new FileOutputStream(tempFile)) {

      if (inputStream == null) {
        throw new FileNotFoundException("Not found: " + resourcePath);
      }

      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    }

    if (os.equals("linux") || os.equals("mac")) {
      tempFile.setExecutable(true);
    }

    return tempFile;
  }

  public static String sanitizeFileName(String fileName) {
    return fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
  }

  public static boolean downloadVideo(String videoUrl, String title) {
    try {
      String userHome = System.getProperty("user.home");

      Path outputDir = Paths.get(userHome, "Downloads");

      File downloadDirectory = outputDir.toFile();
      if (!downloadDirectory.exists()) {
        downloadDirectory.mkdirs();
      }

      File ytDownloader = extractYTDownloader();
      ProcessBuilder processBuilder = new ProcessBuilder(
          ytDownloader.getAbsolutePath(),
          "-o",
          outputDir.resolve(sanitizeFileName(title) + ".mp4").toAbsolutePath().toString(),
          videoUrl);

      Process process = processBuilder.start();

      // BufferedReader reader = new BufferedReader(new
      // InputStreamReader(process.getInputStream()));
      // String line;
      // while ((line = reader.readLine()) != null) {
      // System.out.println(line);
      // }

      int exitCode = process.waitFor();
      if (exitCode != 0) {
        throw new RuntimeException("Video download failed!");
      }

      System.out.println("Video downloaded!");
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean downloadThumbnail(String thumbnailUrl, String title) {
    try {
      String userHome = System.getProperty("user.home");

      Path outputDir = Paths.get(userHome, "Downloads");

      File downloadDirectory = outputDir.toFile();
      if (!downloadDirectory.exists()) {
        downloadDirectory.mkdirs();
      }

      ProcessBuilder processBuilder = new ProcessBuilder(
          "curl",
          "-o",
          outputDir.resolve(sanitizeFileName(title) + ".jpg").toAbsolutePath().toString(),
          thumbnailUrl);

      Process process = processBuilder.start();

      int exitCode = process.waitFor();
      if (exitCode != 0) {
        throw new RuntimeException("Thumbnail download failed!");
      }

      System.out.println("Thumbnail downloaded!");
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static void cleanVideo(String title) {
    try {
      String userHome = System.getProperty("user.home");

      Path outputDir = Paths.get(userHome, "Downloads");

      File videoFile = outputDir.resolve(sanitizeFileName(title) + ".mp4").toFile();
      if (videoFile.exists()) {
        videoFile.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void cleanThumbnail(String title) {
    try {
      String userHome = System.getProperty("user.home");

      Path outputDir = Paths.get(userHome, "Downloads");

      File thumbnailFile = outputDir.resolve(sanitizeFileName(title) + ".jpg").toFile();
      if (thumbnailFile.exists()) {
        thumbnailFile.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static YoutubeData getYouTubeInfo(String videoUrl) {
    try {
      File ytDownloader = extractYTDownloader();

      ProcessBuilder processBuilder = new ProcessBuilder(ytDownloader.getAbsolutePath(), "--dump-json", videoUrl);

      Process process = processBuilder.start();

      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      StringBuilder output = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line);
      }

      JsonObject jsonResponse = JsonParser.parseString(output.toString()).getAsJsonObject();

      String title = jsonResponse.get("title").getAsString();
      System.out.println("Title: " + title);

      String channel = jsonResponse.get("uploader").getAsString();
      System.out.println("Channel: " + channel);

      JsonArray thumbnails = jsonResponse.getAsJsonArray("thumbnails");
      String mediumThumbnailUrl = null;
      for (int i = 0; i < thumbnails.size(); i++) {
        JsonObject thumbnail = thumbnails.get(i).getAsJsonObject();
        JsonElement jsonWidth = thumbnail.get("width");
        if (jsonWidth != null && jsonWidth.getAsInt() > 300 && jsonWidth.getAsInt() <= 360) {
          mediumThumbnailUrl = thumbnail.get("url").getAsString();
          break;
        }
      }
      System.out.println("Thumbnail (medium): " + (mediumThumbnailUrl != null ? mediumThumbnailUrl : "Not found!"));

      YoutubeData data = new YoutubeData(title, channel, mediumThumbnailUrl);
      return data;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    String videoUrl = "https://www.youtube.com/watch?v=qZ9DaB7xRZs";

    getYouTubeInfo(videoUrl);
    // downloadVideo(videoUrl);
  }
}
