package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import modules.AccountManager;
import modules.VerifyData;
import ui.App;
import ui.DefindUI;

public class RegisterController {

  private final String[] avatarHref = {
      RegisterController.class.getResource("/images/avatar_default_1.png").toExternalForm(),
      RegisterController.class.getResource("/images/avatar_default_2.png").toExternalForm(),
      RegisterController.class.getResource("/images/avatar_default_3.png").toExternalForm(),
      RegisterController.class.getResource("/images/avatar_default_4.png").toExternalForm()
  };
  private int avatar = 1;

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
  @FXML
  private Button chooseAvatarButton1;
  @FXML
  private Button chooseAvatarButton2;
  @FXML
  private Button chooseAvatarButton3;
  @FXML
  private Button chooseAvatarButton4;
  @FXML
  private Circle chooseAvarta1;
  @FXML
  private Circle chooseAvarta2;
  @FXML
  private Circle chooseAvarta3;
  @FXML
  private Circle chooseAvarta4;

  public void initialize() {
    // backToHomeButton
    backToHomeButton.toFront();
    backToHomeButton.setOnAction(e -> {
      // back
      App.redirect(DefindUI.getHome());
    });

    // set Avatar Image
    chooseAvarta1.setFill(new ImagePattern(new Image(avatarHref[0])));
    chooseAvarta2.setFill(new ImagePattern(new Image(avatarHref[1])));
    chooseAvarta3.setFill(new ImagePattern(new Image(avatarHref[2])));
    chooseAvarta4.setFill(new ImagePattern(new Image(avatarHref[3])));

    // set default avatar
    handleSetAvatar1();

    // chooseAvatarButton1
    chooseAvatarButton1.setOnAction(e -> {
      handleSetAvatar1();
    });

    // chooseAvatarButton2
    chooseAvatarButton2.setOnAction(e -> {
      handleSetAvatar2();
    });

    // chooseAvatarButton3
    chooseAvatarButton3.setOnAction(e -> {
      handleSetAvatar3();
    });

    // chooseAvatarButton4
    chooseAvatarButton4.setOnAction(e -> {
      handleSetAvatar4();
    });
  }

  // Submit handler
  @FXML
  protected void handleRegister() {
    String username = txtUsername.getText();
    String email = txtEmail.getText();
    String password = txtPassword.getText();

    // verify data
    // email
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

    // username
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

    // password
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

    // render avatar href
    String href = avatarHref[avatar - 1];

    // Start register
    System.out.println("Username: " + username);
    System.out.println("Password: " + password);
    if (AccountManager.register(username, email, password, href)) {
      actionMessage.setText("Đăng ký thành công!");
      App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
    } else {
      actionMessage.setText("Tài khoản đã tồn tại! Vui lòng thử lại với email hoặc username khác.");
    }
  }

  // setStyle avatar
  protected void handleSetAvatar1() {
    avatar = 1;
    chooseAvarta1.setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 2;");
    chooseAvarta2.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
    chooseAvarta3.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
    chooseAvarta4.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
  }

  protected void handleSetAvatar2() {
    avatar = 2;
    chooseAvarta1.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
    chooseAvarta2.setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 2;");
    chooseAvarta3.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
    chooseAvarta4.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
  }

  protected void handleSetAvatar3() {
    avatar = 3;
    chooseAvarta1.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
    chooseAvarta2.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
    chooseAvarta3.setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 2;");
    chooseAvarta4.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
  }

  protected void handleSetAvatar4() {
    avatar = 4;
    chooseAvarta1.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
    chooseAvarta2.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
    chooseAvarta3.setStyle("-fx-stroke: #000000; -fx-stroke-width: 0;");
    chooseAvarta4.setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 2;");
  }

  // Redirect to login
  @FXML
  protected void handleToLogin() {
    App.redirect(DefindUI.getNoLayout(), DefindUI.getLogin());
  }
}
