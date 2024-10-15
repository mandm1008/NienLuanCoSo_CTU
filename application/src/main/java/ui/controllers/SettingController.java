package ui.controllers;

import java.util.LinkedList;

import db.PlaylistModel;
import db.SettingModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import modules.AccountManager;

public class SettingController {
  @FXML
  private TextField nameField;
  @FXML
  private CheckBox nameCheck;
  @FXML
  private Button nameButton;
  @FXML
  private Button passChangeButton;
  @FXML
  private Button passBackupButton;
  @FXML
  private ChoiceBox<String> playlistDefault;
  @FXML
  private TextField volumeField;
  @FXML
  private CheckBox shuffCheckBox;
  @FXML
  private CheckBox playNowCheckBox;
  @FXML
  private Button saveSettingButton;

  LinkedList<PlaylistModel> playlists;

  public void initialize() {
    // Load the setting
    Platform.runLater(loadSetting());

    // Add event on login
    AccountManager.addEventLogin("setting", loadSetting());

    // Add event on logout
    AccountManager.addEventLogout("setting", () -> {
      nameField.setText("");
      playlistDefault.setValue("");
      volumeField.setText("");
      shuffCheckBox.setSelected(false);
      playNowCheckBox.setSelected(false);
    });

    // Add event on change playlist
    AccountManager.addEventChangePlaylist("setting", loadSetting());

    // save setting
    saveSettingButton.setOnAction(event -> handleSaveSettings());

    // handle username
    handleUsername();
  }

  private void handleUsername() {
    nameCheck.onActionProperty().set(event -> {
      if (nameCheck.isSelected()) {
        nameField.setDisable(false);
      } else {
        nameField.setDisable(true);
        nameButton.setDisable(true);
        nameField.setText(AccountManager.getUsername());
      }
    });

    nameField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.equals(AccountManager.getUsername())) {
        nameButton.setDisable(true);
      } else {
        nameButton.setDisable(false);
      }
    });

    nameButton.setOnAction(event -> {
      AccountManager.updateUsername(nameField.getText());

      nameButton.setDisable(true);
      nameCheck.setSelected(false);
      nameField.setDisable(true);
      nameField.setText(AccountManager.getUsername());
    });
  }

  private Runnable loadSetting() {
    return () -> {
      // check login
      if (AccountManager.getId() <= 0) {
        return;
      }

      // set null playlist
      playlists = null;

      // load choice box
      Platform.runLater(handleChoiceBox());

      // Load the setting
      SettingModel setting = AccountManager.getSetting();

      // Set the setting to the UI
      PlaylistModel pl = setting.getPlaylist();
      nameField.setText(AccountManager.getUsername());
      if (pl != null) {
        playlistDefault.setValue(pl.getName());
      } else {
        playlistDefault.setValue("Mới nhất");
      }
      volumeField.setText(String.valueOf(setting.getVolume()));
      shuffCheckBox.setSelected(setting.getShuff());
      playNowCheckBox.setSelected(setting.getPlayNow());
    };
  }

  private Runnable handleChoiceBox() {
    return () -> {
      playlistDefault.getItems().clear();
      if (playlists == null)
        playlists = AccountManager.getPlaylists();

      playlistDefault.getItems().add("Mới nhất");
      playlistDefault.getItems().add("Nhiều lượt nghe");

      for (PlaylistModel playlist : playlists) {
        playlistDefault.getItems().add(playlist.getName());
      }
    };
  }

  private void handleSaveSettings() {
    // Get the setting
    SettingModel setting = AccountManager.getSetting();

    // Set the setting
    String playlistDefaultValue = playlistDefault.getValue();
    if (playlistDefaultValue == "Mới nhất") {
      setting.setPlaylistId(0);
    } else if (playlistDefaultValue == "Nhiều lượt nghe") {
      setting.setPlaylistId(1);
    } else {
      setting.setPlaylistId(playlists.stream().filter(playlist -> playlist.getName().equals(playlistDefaultValue))
          .findFirst().get().getPlaylistId());
    }
    setting.setVolume(Integer.parseInt(volumeField.getText()));
    setting.setShuff(shuffCheckBox.isSelected());
    setting.setPlayNow(playNowCheckBox.isSelected());

    // Save the setting
    AccountManager.saveSetting();
  }
}
