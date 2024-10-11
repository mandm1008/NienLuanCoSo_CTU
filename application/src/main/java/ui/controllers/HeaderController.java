package ui.controllers;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import modules.AccountManager;
import modules.SearchManager;
import ui.App;
import ui.DefindUI;

public class HeaderController {
  @FXML
  private Button homeBtn;
  @FXML
  private Button reloadBtn;
  @FXML
  private HBox searchBox;
  @FXML
  private Button searchBtn;
  @FXML
  private TextField searchField;
  @FXML
  private Button avartaButton;
  @FXML
  private Circle avartaBox;
  @FXML
  private ContextMenu avartaMenu;

  private RotateTransition rotateTransition = null;
  private boolean isStopRotate = false;

  public void initialize() {
    // setup search
    setupSearch();

    // home Btn
    toHome();

    // reload Btn
    App.addEventReloadAll("header-reload-button", reloadAll());

    // update avatar
    updateAvatar().run();
    String key = "avatar-header";
    AccountManager.addEventLogin(key, updateAvatar());
    AccountManager.addEventLogout(key, updateAvatar());

    // setup menu
    setupMenu();
  }

  private void toHome() {
    homeBtn.setOnAction(e -> {
      Platform.runLater(() -> {
        App.redirect(DefindUI.getHome());
      });
    });
  }

  private Runnable reloadAll() {
    // create rotate reload button
    if (rotateTransition == null) {
      rotateTransition = new RotateTransition(Duration.millis(2000), reloadBtn);
      rotateTransition.setByAngle(360);
      rotateTransition.setCycleCount(1);
      rotateTransition.setOnFinished(e -> {
        if (isStopRotate) {
          rotateTransition.stop();
          isStopRotate = false;
        } else {
          rotateTransition.play();
        }
      });
    }

    reloadBtn.setOnAction(e -> {
      // start rotate
      rotateTransition.play();
      App.reload();
    });

    return () -> {
      // stop rotate
      isStopRotate = true;
    };
  }

  private void setupSearch() {
    // search button
    searchBtn.setOnAction(e -> {
      // check empty
      if (searchField.getText().isEmpty()) {
        return;
      }

      // handle search
      SearchManager searchManager = App.getSearchManager();
      searchManager.search(searchField.getText());
    });

    // search enter
    searchField.setOnKeyPressed(e -> {
      if (e.getCode() == SearchManager.KEY_CODE) {
        // check empty
        if (searchField.getText().isEmpty()) {
          return;
        }

        // handle search
        SearchManager searchManager = App.getSearchManager();
        searchManager.search(searchField.getText());
      }
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

  private void setupMenu() {
    // create menu
    avartaMenu = new ContextMenu();

    // add menu item
    MenuItem settingItem = new MenuItem("Cài đặt");
    MenuItem upLoadSong = new MenuItem("Tải lên bài hát");
    MenuItem logoutItem = new MenuItem("Đăng xuất");

    // add css
    String itemStyle = "-fx-background-color: transparent;" +
        "-fx-background-insets: 0;" +
        "-fx-border-color: transparent;" +
        "-fx-border-width: 0;" +
        "-fx-effect: none;" +
        "-fx-cursor: hand;" +
        "-fx-text-fill: #fff;" +
        "-fx-font-weight: bold;" +
        "-fx-padding: 4px 12px;";
    avartaMenu.setStyle(
        "-fx-background-color: #1E1E2E;" +
            " -fx-padding: 10px;" +
            "-fx-border-color: #fff;" +
            " -fx-border-width: 1px;" +
            "-fx-border-radius: 5px;" +
            " -fx-background-radius: 5px;");
    settingItem.setStyle(itemStyle);
    upLoadSong.setStyle(itemStyle);
    logoutItem.setStyle(itemStyle);

    settingItem.setOnAction(event -> {
      // handle setting
      System.out.println("Go to Setting");
    });

    upLoadSong.setOnAction(event -> {
      // check user login
      if (AccountManager.getId() <= -1) {
        App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
        return;
      }

      // handle upload song
      App.redirect(DefindUI.getUpload());
    });

    logoutItem.setOnAction(event -> {
      // handle logout
      AccountManager.logout();
      App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
    });

    // add item to menu
    avartaMenu.getItems().addAll(settingItem, upLoadSong, logoutItem);

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
}
