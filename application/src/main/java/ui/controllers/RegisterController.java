package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import modules.AccountManager;
import modules.VerifyData;
import ui.App;
import ui.DefindUI;

public class RegisterController {

  @FXML
  private TextField txtUsername;

  @FXML
  private TextField txtEmail;

  @FXML
  private PasswordField txtPassword;

  @FXML
  private Text actionMessage;

  @FXML
  private Button toLogin;

  @FXML
  private Button backToHomeButton;

  public void initialize() {
    // backToHomeButton
    backToHomeButton.toFront();
    backToHomeButton.setOnAction(e -> {
      // back
      App.redirect(DefindUI.getHome());
    });
  }

  // Submit handler
  @FXML
  protected void handleRegister() {
    String username = txtUsername.getText();
    String email = txtEmail.getText();
    String password = txtPassword.getText();

    // verify data
    if (email.isEmpty()) {
      actionMessage.setText("Chưa nhập email!");
      txtEmail.requestFocus();
      return;
    }

    if (!VerifyData.verifyEmail(email)) {
      actionMessage.setText("Email không tồn tại!");
      txtEmail.requestFocus();
      return;
    }

    if (username.isEmpty()) {
      actionMessage.setText("Chưa nhập username!");
      txtUsername.requestFocus();
      return;
    }

    if (!VerifyData.verifyUsername(username)) {
      actionMessage.setText("Username phải có ít nhất 6 ký tự!");
      txtUsername.requestFocus();
      return;
    }

    if (password.isEmpty()) {
      actionMessage.setText("Chưa nhập password!");
      txtPassword.requestFocus();
      return;
    }

    if (!VerifyData.verifyPassword(password)) {
      actionMessage.setText(
          "Password phải có ít nhất 8 ký tự, 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt!");
      txtPassword.requestFocus();
      return;
    }

    // Start register
    System.out.println("Username: " + username);
    System.out.println("Password: " + password);
    if (AccountManager.register(username, email, password)) {
      actionMessage.setText("Đăng ký thành công!");
      App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
    } else {
      actionMessage.setText("Tài khoản đã tồn tại! Vui lòng thử lại với email hoặc username khác.");
    }
  }

  // Redirect to login
  @FXML
  protected void handleToLogin() {
    App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
  }
}
