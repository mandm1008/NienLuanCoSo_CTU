package modules;

import java.util.HashMap;

import javafx.scene.image.Image;

public class ImageManager {
  private static HashMap<String, Image> images = new HashMap<>();
  public static String BANNER = "banner";
  public static String LIKE = "like";
  public static String LIKED = "liked";
  public static String PLAY = "play";
  public static String PAUSE = "pause";
  public static String LOADING = "loading";
  public static String VOLUMEON = "volumeon";
  public static String VOLUMEOFF = "volumeoff";
  public static String DEMO_MUSIC = "demo_music";
  public static String MENU = "menu";
  public static String PLAY_ADD = "play_add";

  public static void loadImages() {
    images.put(BANNER, new Image(ImageManager.class.getResource("/images/banner-solid.png").toExternalForm()));
    images.put(LIKE, new Image(ImageManager.class.getResource("/images/heart-regular.png").toExternalForm()));
    images.put(LIKED, new Image(ImageManager.class.getResource("/images/heart-solid.png").toExternalForm()));
    images.put(PLAY, new Image(ImageManager.class.getResource("/images/play-solid.png").toExternalForm()));
    images.put(PAUSE, new Image(ImageManager.class.getResource("/images/pause-solid.png").toExternalForm()));
    images.put(LOADING, new Image(ImageManager.class.getResource("/images/loading.gif").toExternalForm()));
    images.put(VOLUMEON, new Image(ImageManager.class.getResource("/images/volume-high-solid.png").toExternalForm()));
    images.put(VOLUMEOFF, new Image(ImageManager.class.getResource("/images/volume-xmark-solid.png").toExternalForm()));
    images.put(DEMO_MUSIC, new Image(ImageManager.class.getResource("/images/demo_music.png").toExternalForm()));
    images.put(MENU, new Image(ImageManager.class.getResource("/images/ellipsis-vertical-solid.png").toExternalForm()));
    images.put(PLAY_ADD, new Image(ImageManager.class.getResource("/images/square-plus-solid.png").toExternalForm()));
  }

  public static Image getImage(String key) {
    return images.get(key);
  }
}
