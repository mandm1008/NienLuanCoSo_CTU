package modules;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.App;

public class CustomDialog {
  private Stage stage;
  private NotificationManager notificationManager;

  public CustomDialog() {
    this.stage = new Stage();
    this.stage.initModality(Modality.WINDOW_MODAL);
    this.stage.setMaximized(true);
    this.notificationManager = new NotificationManager(this.stage);
  }

  public CustomDialog(String title) {
    this();
    this.setTitle(title);
  }

  public void setTitle(String title) {
    this.stage.setTitle(title);
  }

  public void loadContent(Parent root) {
    Scene scene = new Scene(root);
    scene.getStylesheets().add(CustomDialog.class.getResource("/css/scene.css").toExternalForm());
    scene.setFill(Paint.valueOf("#1e1e2e"));
    stage.getIcons().add(new Image(App.class.getResource("/images/banner-solid.png").toExternalForm()));
    this.stage.setScene(scene);
  }

  public void show() {
    this.stage.show();
  }

  public void close() {
    this.stage.close();
  }

  public NotificationManager getNotificationManager() {
    return this.notificationManager;
  }

}
