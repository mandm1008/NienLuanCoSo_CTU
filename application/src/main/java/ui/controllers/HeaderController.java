package ui.controllers;

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
    // avarta init
    String avatarSrc = HeaderController.class.getResource("/images/avatar_default.jpg").toExternalForm();
    if (AccountManager.getId() > -1) {
      // change avatar if has user

    }

    Image image = new Image(avatarSrc);
    avartaBox.setFill(new ImagePattern(image));

    // avarta action
    avartaButton.setOnAction(e -> {
      // if no login
      if (AccountManager.getId() <= -1) {
        App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
        return;
      }

      // handle visible box
      avartaMenu = new ContextMenu();
      // add menu item
      MenuItem settingItem = new MenuItem("Setting");
      MenuItem logoutItem = new MenuItem("Logout");

      // add css
      avartaMenu.setStyle("-fx-background-color: #1E1E2E;");
      settingItem.setStyle(
          "-fx-background-color: transparent;" +
              "-fx-background-insets: 0;" +
              "-fx-border-color: transparent;" +
              "-fx-border-width: 0;" +
              "-fx-effect: none;" +
              "-fx-cursor: hand;" +
              "-fx-text-fill: #fff;" +
              "-fx-font-weight: bold;" +
              "-fx-padding: 8px 4px;");
      logoutItem.setStyle(
          "-fx-background-color: transparent;" +
              "-fx-background-insets: 0;" +
              "-fx-border-color: transparent;" +
              "-fx-border-width: 0;" +
              "-fx-effect: none;" +
              "-fx-cursor: hand;" +
              "-fx-text-fill: #fff;" +
              "-fx-font-weight: bold;" +
              "-fx-padding: 8px 4px;");

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
      avartaMenu.show(avartaButton, avartaButton.getLayoutX(), avartaButton.getLayoutY());
    });
  }
}
