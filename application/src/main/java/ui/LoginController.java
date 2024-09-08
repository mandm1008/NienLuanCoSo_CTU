package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import modules.AccountManager;

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

  // Submit handler
  @FXML
  protected void handleLogin() {
    String username = txtUsername.getText();
    String password = txtPassword.getText();

    // verify data
    if (username.isEmpty()) {
      actionMessage.setText("Username is required!");
      txtUsername.requestFocus();
      return;
    }

    if (password.isEmpty()) {
      actionMessage.setText("Password is required!");
      txtPassword.requestFocus();
      return;
    }

    // Start login
    System.out.println("Username: " + username);
    System.out.println("Password: " + password);
    if (AccountManager.login(username, password)) {
      actionMessage.setText("");
      App.redirect(DefindUI.getLayout(), DefindUI.getHome());
    } else {
      actionMessage.setText("Username or password is incorrect! Please try again.");
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
}
