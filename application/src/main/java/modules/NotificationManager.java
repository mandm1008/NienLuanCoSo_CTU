package modules;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import javafx.util.Duration;

public class NotificationManager {
  public static final String SUCCESS = "success";
  public static final String ERROR = "error";
  public static final String WARNING = "warning";

  private Popup popup;
  private VBox container;
  private Stage stage;
  private int count = 0;

  public NotificationManager(Stage primaryStage) {
    this.stage = primaryStage;
    this.container = new VBox(10);
    this.container.setAlignment(Pos.TOP_RIGHT);
    this.container.setPadding(new Insets(10));
    this.container.setStyle("-fx-background-color: transparent;");

    popup = new Popup();
    popup.getContent().add(container);

    // set position
    popup.setX(primaryStage.getX() + primaryStage.getWidth() - 220);
    popup.setY(primaryStage.getY() + 100);
  }

  public void addCount() {
    count++;
    Platform.runLater(() -> {
      popup.show(stage);
    });
  }

  public void removeCount() {
    count--;
    if (count == 0) {
      Platform.runLater(() -> {
        popup.hide();
      });
    }
  }

  public void notify(String message, String type) {
    // check type
    String backgroundColor = "#2C3E50";
    switch (type) {
      case SUCCESS:
        message = "✔ " + message;
        backgroundColor = "#2ECC71";
        break;
      case ERROR:
        message = "❌ " + message;
        backgroundColor = "#E74C3C";
        break;
      case WARNING:
        message = "⚠ " + message;
        backgroundColor = "#F1C40F";
        break;
      default:
        break;
    }

    // create notification label
    Label label = new Label(message);
    label
        .setStyle("-fx-background-color: " + backgroundColor
            + "; -fx-text-fill: white; -fx-padding: 10px;-fx-background-radius: 5px;");
    label.setMaxWidth(200);
    label.setPrefWidth(200);
    label.setWrapText(true);
    label.setPadding(new Insets(10));
    label.setOpacity(0);

    // add notification
    container.getChildren().add(label);

    // fade-in
    Timeline showTimeline = new Timeline(
        new KeyFrame(Duration.ZERO, new KeyValue(label.opacityProperty(), 0.0)),
        new KeyFrame(Duration.seconds(0.5), new KeyValue(label.opacityProperty(), 1.0)));

    showTimeline.setOnFinished(_ -> {
      // fade-out
      Timeline hideTimeline = new Timeline(
          new KeyFrame(Duration.seconds(2), new KeyValue(label.opacityProperty(), 1.0)),
          new KeyFrame(Duration.seconds(3), new KeyValue(label.opacityProperty(), 0.0)));
      hideTimeline.setOnFinished(_ -> {
        // delete notificationLabel after hide
        container.getChildren().remove(label);
        removeCount();
      });
      hideTimeline.play();
    });

    addCount();
    showTimeline.play();
  }
}
