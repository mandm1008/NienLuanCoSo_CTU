package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import modules.AccountManager;
import modules.VerifyData;

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

  // Submit handler
  @FXML
  protected void handleRegister() {
    String username = txtUsername.getText();
    String email = txtEmail.getText();
    String password = txtPassword.getText();

    // verify data
    if (email.isEmpty()) {
      actionMessage.setText("Email is required!");
      txtEmail.requestFocus();
      return;
    }

    if (!VerifyData.verifyEmail(email)) {
      actionMessage.setText("Invalid email format!");
      txtEmail.requestFocus();
      return;
    }

    if (username.isEmpty()) {
      actionMessage.setText("Username is required!");
      txtUsername.requestFocus();
      return;
    }

    if (!VerifyData.verifyUsername(username)) {
      actionMessage.setText("Username must be at least 6 characters!");
      txtUsername.requestFocus();
      return;
    }

    if (password.isEmpty()) {
      actionMessage.setText("Password is required!");
      txtPassword.requestFocus();
      return;
    }

    if (!VerifyData.verifyPassword(password)) {
      actionMessage.setText(
          "Password must be at least 8 characters, contain at least one digit, one upper case, one lower case and one special character!");
      txtPassword.requestFocus();
      return;
    }

    // Start register
    System.out.println("Username: " + username);
    System.out.println("Password: " + password);
    if (AccountManager.register(username, email, password)) {
      actionMessage.setText("Register success");
      App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
    } else {
      actionMessage.setText("Account already exists! Please try with another username or email.");
    }
  }

  // Redirect to login
  @FXML
  protected void handleToLogin() {
    App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
  }
}
