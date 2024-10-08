package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import modules.AccountManager;
import modules.CustomDialog;
import modules.NotificationManager;
import ui.App;

public class PlaylistCreateController {
  @FXML
  private TextField nameField;
  @FXML
  private Button createButton;
  @FXML
  private Button cancelButton;

  private CustomDialog dialog;

  public void initialize() {
    createButton.setOnAction(e -> {
      String name = nameField.getText();
      if (name.isEmpty()) {
        return;
      }

      // create playlist
      if (AccountManager.createPlaylist(name)) {
        nameField.clear();
        // notify
        App.getNotificationManager().notify("Tạo danh sách thành công!", NotificationManager.SUCCESS);
        // close dialog
        dialog.close();
      } else {
        dialog.getNotificationManager().notify("Bạn đã có danh sách tên này! Thử lại với tên khác!",
            NotificationManager.ERROR);
      }
    });

    cancelButton.setOnAction(e -> {
      dialog.close();
    });
  }

  @SuppressWarnings("exports")
  public void setDialog(CustomDialog dialog) {
    this.dialog = dialog;
  }
}
