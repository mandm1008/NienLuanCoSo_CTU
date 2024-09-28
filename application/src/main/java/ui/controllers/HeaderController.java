package ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import modules.AccountManager;
import ui.App;
import ui.DefindUI;

public class HeaderController {

  @FXML
  private Button backBtn;

  @FXML
  private Button nextBtn;

  @FXML
  private HBox searchBox;

  @FXML
  private TextField searchField;

  @FXML
  private Button avartaButton;

  @FXML
  private Circle avartaBox;

  @FXML
  private ContextMenu avartaMenu;

  @FXML
  public void initialize() {
    updateAvatar().run();
    // add event
    String key = "avatar-header";
    AccountManager.addEventLogin(key, updateAvatar());

    // create menu
    avartaMenu = new ContextMenu();
    // add menu item
    MenuItem settingItem = new MenuItem("Setting");
    MenuItem logoutItem = new MenuItem("Logout");

    // add css
    avartaMenu
        .setStyle("-fx-background-color: #1E1E2E;-fx-padding: 10px;-fx-border-color: #fff;-fx-border-width: 2px;");
    settingItem.setStyle(
        "-fx-background-color: transparent;" +
            "-fx-background-insets: 0;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0;" +
            "-fx-effect: none;" +
            "-fx-cursor: hand;" +
            "-fx-text-fill: #fff;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 4px 12px;");
    logoutItem.setStyle(
        "-fx-background-color: transparent;" +
            "-fx-background-insets: 0;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0;" +
            "-fx-effect: none;" +
            "-fx-cursor: hand;" +
            "-fx-text-fill: #fff;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 4px 12px;");

    settingItem.setOnAction(event -> {
      // handle setting
      System.out.println("Go to Setting");
    });

    logoutItem.setOnAction(event -> {
      // handle logout
      AccountManager.logout();
      App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
    });

    avartaMenu.getItems().addAll(settingItem, logoutItem);

    // avarta action
    avartaButton.setOnAction(e -> {
      // if no login
      if (AccountManager.getId() <= -1) {
        App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
        return;
      }

      double screenX = avartaButton.localToScreen(avartaButton.getBoundsInLocal()).getMinX() + avartaButton.getWidth()
          - 100;
      double screenY = avartaButton.localToScreen(avartaButton.getBoundsInLocal()).getMinY() + avartaButton.getHeight();

      // visible menu
      avartaMenu.show(avartaButton, screenX, screenY);
    });
  }

  private Runnable updateAvatar() {
    return () -> {
      String avatarSrc = HeaderController.class.getResource("/images/avatar_default.png").toExternalForm();
      if (AccountManager.getId() > -1) {
        // change avatar if has user
        avatarSrc = AccountManager.getAvatar();
      }

      Image image = new Image(avatarSrc);
      Platform.runLater(() -> {
        avartaBox.setFill(new ImagePattern(image));
      });
    };
  }
}
