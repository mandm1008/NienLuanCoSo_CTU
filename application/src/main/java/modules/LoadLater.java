package modules;

import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.util.Callback;

public class LoadLater {
  private static HashMap<String, Image> images = new HashMap<>();

  public static void addLoader(String src, Callback<Image, Void> callback) {
    new Thread(() -> {
      if (images.containsKey(src)) {
        callback.call(images.get(src));
        return;
      }

      Image image = new Image(src);

      if (image.isError()) {
        addLoader(src, callback);
        return;
      }

      images.put(src, image);

      callback.call(image);
    }).start();
  }
}
