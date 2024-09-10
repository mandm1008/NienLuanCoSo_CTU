package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import modules.AccountManager;
import ui.App;
import ui.DefindUI;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

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
  public void initialize() {
    // avarta init
    String avatarSrc = HeaderController.class.getResource("/images/demo_music.jpg").toExternalForm();
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

    });
  }
}
