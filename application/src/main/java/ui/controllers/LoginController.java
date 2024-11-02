package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import modules.AccountManager;
import modules.NotificationManager;
import ui.App;
import ui.DefindUI;

public class LoginController {

  @FXML
  private TextField txtUsername;
  @FXML
  private PasswordField txtPassword;
  @FXML
  private Button forgotPassword;
  @FXML
  private Button createNew;
  @FXML
  private Text actionMessage;
  @FXML
  private Button backToHomeButton;

  public void initialize() {
    // backToHomeButton
    backToHomeButton.toFront();
    backToHomeButton.setOnAction(_ -> {
      // back
      System.out.println("Back to Home");
      App.redirect(DefindUI.getHome());
    });

    // clear errors
    txtUsername.setOnKeyTyped(_ -> clearErrors());
    txtPassword.setOnKeyTyped(_ -> clearErrors());
  }

  // Submit handler
  @FXML
  protected void handleLogin() {
    String username = txtUsername.getText();
    String password = txtPassword.getText();

    // verify data
    if (username.isEmpty()) {
      actionMessage.setText("Chưa nhập username!");
      txtUsername.requestFocus();
      return;
    }

    if (password.isEmpty()) {
      actionMessage.setText("Chưa nhập password!");
      txtPassword.requestFocus();
      return;
    }

    // Start login
    if (AccountManager.login(username, password)) {
      actionMessage.setText("");
      App.getNotificationManager().notify("Đăng nhập thành công! Tài khoản: " + username, NotificationManager.SUCCESS);

      App.redirect(DefindUI.getLayout(), DefindUI.getHome());
    } else {
      actionMessage.setText("Username hoặc Password không đúng! Vui lòng thử lại.");
    }
  }

  @FXML
  protected void handleForgotPassword() {
    // redirect to forgot password page
    App.redirect(DefindUI.getNoLayout(), DefindUI.getRegister()); // comming soon
  }

  @FXML
  protected void handleCreateNew() {
    App.redirect(DefindUI.getNoLayout(), DefindUI.getRegister());
  }

  private void clearErrors() {
    actionMessage.setText("");
  }
}
