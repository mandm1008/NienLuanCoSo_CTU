package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import modules.AccountManager;
import modules.VerifyData;
import ui.App;
import ui.DefindUI;

public class ChangePasswordController {
  @FXML
  private TextField oldPasswordField;
  @FXML
  private TextField newPasswordField;
  @FXML
  private TextField confirmPasswordField;
  @FXML
  private Button changePasswordButton;
  @FXML
  private Button backButton;
  @FXML
  private Text actionMessage;

  public void initialize() {
    // back
    backButton.setOnAction(event -> handleBack());

    // change password
    changePasswordButton.setOnAction(event -> handleChangePassword());

    // clear error
    oldPasswordField.setOnKeyTyped(event -> clearError());
    newPasswordField.setOnKeyTyped(event -> clearError());
    confirmPasswordField.setOnKeyTyped(event -> clearError());
  }

  public void handleBack() {
    // to setting
    App.redirect(DefindUI.getSetting());
  }

  public void handleChangePassword() {
    // change password
    String oldPassword = oldPasswordField.getText();
    String newPassword = newPasswordField.getText();
    String confirmPassword = confirmPasswordField.getText();

    if (oldPassword.isEmpty()) {
      actionMessage.setText("Vui lòng nhập mật khẩu cũ");
      oldPasswordField.requestFocus();
      return;
    }

    if (newPassword.isEmpty()) {
      actionMessage.setText("Vui lòng nhập mật khẩu mới");
      newPasswordField.requestFocus();
      return;
    }

    if (!VerifyData.verifyPassword(newPassword)) {
      actionMessage.setText("Mật khẩu phải có ít nhất 8 ký tự, 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt");
      newPasswordField.requestFocus();
      return;
    }

    if (confirmPassword.isEmpty()) {
      actionMessage.setText("Vui lòng xác nhận lại mật khẩu");
      confirmPasswordField.requestFocus();
      return;
    }

    if (!newPassword.equals(confirmPassword)) {
      actionMessage.setText("Mật khẩu mới không khớp");
      confirmPasswordField.requestFocus();
      return;
    }

    // change password
    if (AccountManager.changePassword(oldPassword, newPassword)) {
      actionMessage.setText("Đổi mật khẩu thành công");
    } else {
      actionMessage.setText("Mật khẩu cũ không đúng");
      oldPasswordField.requestFocus();
    }
  }

  private void clearError() {
    actionMessage.setText("");
  }
}
