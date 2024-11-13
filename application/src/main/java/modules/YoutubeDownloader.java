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
import java.util.HashMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class YoutubeDownloader {
  public static final String DOWNLOAD_DIR = "downloads";

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

  // cache the extracted yt-dlp file
  private static File YTDLPFile = null;

  public static File extractYTDownloader() throws IOException {
    if (YTDLPFile != null) {
      return YTDLPFile;
    }

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

    YTDLPFile = tempFile;

    return tempFile;
  }

  public static void convertWebpToJpg(String inputFilePath, String outputFilePath) {
    try {
      File webpFile = new File(inputFilePath);

      String mimeType = Files.probeContentType(webpFile.toPath());

      if (mimeType != null && mimeType.equals("image/webp")) {
        BufferedImage webpImage = ImageIO.read(webpFile);

        File jpgFile = new File(outputFilePath);
        ImageIO.write(webpImage, "jpg", jpgFile);

        System.out.println("Chuyển đổi thành công từ WebP sang JPG!");
      } else {
        System.out.println("Tệp không phải là định dạng WebP.");
      }
    } catch (IOException e) {
      System.err.println("Đã xảy ra lỗi: " + e.getMessage());
    }
  }

  public static String sanitizeFileName(String fileName) {
    return fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
  }

  public static Path storeDir() {
    Path jarDir = Paths.get("").toAbsolutePath();

    Path outputDir = jarDir.resolve(DOWNLOAD_DIR).normalize();

    File downloadDirectory = outputDir.toFile();

    if (!downloadDirectory.exists()) {
      downloadDirectory.mkdirs();
    }

    return outputDir;
  }

  public static String downloadVideo(String videoUrl, String title) {
    try {
      Path outputDir = storeDir();

      String pathToFile = outputDir.resolve(sanitizeFileName(title) + ".mp4").toAbsolutePath().toString();

      // check if video is already downloaded
      File videoFile = new File(pathToFile);
      if (videoFile.exists()) {
        return pathToFile;
      }

      File ytDownloader = extractYTDownloader();
      ProcessBuilder processBuilder = new ProcessBuilder(
          ytDownloader.getAbsolutePath(),
          "-o",
          pathToFile,
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
      return pathToFile;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String downloadThumbnail(String thumbnailUrl, String title) {
    try {
      Path outputDir = storeDir();

      String pathToFileWebp = outputDir.resolve(sanitizeFileName(title) + ".webp").toAbsolutePath().toString();
      String pathToFile = outputDir.resolve(sanitizeFileName(title) + ".jpg").toAbsolutePath().toString();

      // check if thumbnail is already downloaded
      File thumbnailFile = new File(pathToFile);
      if (thumbnailFile.exists()) {
        return pathToFile;
      }

      ProcessBuilder processBuilder = new ProcessBuilder(
          "curl",
          "-o",
          pathToFileWebp,
          thumbnailUrl);

      Process process = processBuilder.start();

      int exitCode = process.waitFor();
      if (exitCode != 0) {
        throw new RuntimeException("Thumbnail download failed!");
      }

      convertWebpToJpg(pathToFileWebp, pathToFile);

      System.out.println("Thumbnail downloaded!");
      return pathToFile;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void cleanVideo(YoutubeData data) {
    cleanVideo(data.getTitle());
  }

  public static void cleanVideo(String title) {
    try {
      Path outputDir = storeDir();

      File videoFile = outputDir.resolve(sanitizeFileName(title) + ".mp4").toFile();
      if (videoFile.exists()) {
        videoFile.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void cleanThumbnail(YoutubeData data) {
    cleanThumbnail(data.getTitle());
  }

  public static void cleanThumbnail(String title) {
    try {
      Path outputDir = storeDir();

      File thumbnailFile = outputDir.resolve(sanitizeFileName(title) + ".jpg").toFile();
      if (thumbnailFile.exists()) {
        thumbnailFile.delete();
      }
      File thumbnailWebpFile = outputDir.resolve(sanitizeFileName(title) + ".webp").toFile();
      if (thumbnailWebpFile.exists()) {
        thumbnailWebpFile.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // cache youtube data
  private static HashMap<String, YoutubeData> cacheYTData = new HashMap<>();

  public static YoutubeData getYouTubeInfo(String videoUrl) {
    if (cacheYTData.containsKey(videoUrl)) {
      return cacheYTData.get(videoUrl);
    }

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
        if (jsonWidth != null && jsonWidth.getAsInt() >= 360 && jsonWidth.getAsInt() <= 1080) {
          mediumThumbnailUrl = thumbnail.get("url").getAsString();
          if (!mediumThumbnailUrl.contains(".webp") && mediumThumbnailUrl.contains("?"))
            break;
        }
      }
      System.out.println("Thumbnail: " + (mediumThumbnailUrl != null ? mediumThumbnailUrl : "Not found!"));

      YoutubeData data = new YoutubeData(title, channel, mediumThumbnailUrl);

      cacheYTData.put(videoUrl, data);

      return data;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void cleanAll() {
    cacheYTData.clear();
    try {
      Path outputDir = storeDir();

      File downloadDirectory = outputDir.toFile();
      File[] files = downloadDirectory.listFiles();
      for (File file : files) {
        file.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String videoUrl = "https://www.youtube.com/watch?v=qzrv-g06yhU";

    YoutubeData d = getYouTubeInfo(videoUrl);
    // downloadVideo(videoUrl);
    String href = downloadThumbnail(d.getThumbnail(), d.getTitle());
    System.out.println("Thumbnail: " + href);
  }
}
