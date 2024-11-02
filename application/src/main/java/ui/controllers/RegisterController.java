package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import modules.AccountManager;
import modules.LoadLater;
import modules.NotificationManager;
import modules.VerifyData;
import ui.App;
import ui.DefindUI;

// https://drive.google.com/uc?export=download&id=FILE_ID

public class RegisterController {

  private final String[] avatarHref = {
      "https://drive.google.com/uc?export=download&id=1zESWNLrldqQdbxLXMrteSsDzyIU_T9WL",
      "https://drive.google.com/uc?export=download&id=1AMezsCUuTEsxNPxlN2p3eLbh4_tunT4c",
      "https://drive.google.com/uc?export=download&id=1nkoR7UdOpNPMCQNorWEgXPhsRUWV7Cn6",
      "https://drive.google.com/uc?export=download&id=1ybOWcoDMz4K3MjI4c9Nt2PZZGOvSqEnZ"
  };
  private int avatar = 1;

  @FXML
  private TextField txtUsername;
  @FXML
  private TextField txtEmail;
  @FXML
  private PasswordField txtPassword;
  @FXML
  private PasswordField txtConfirmPassword;
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
    backToHomeButton.setOnAction(_ -> {
      // back
      App.redirect(DefindUI.getHome());
    });

    // set avatar image load later
    Callback<Image, Void> loader1 = new Callback<Image, Void>() {
      @Override
      public Void call(Image arg0) {
        chooseAvarta1.setFill(new ImagePattern(arg0));
        return null;
      }
    };
    LoadLater.addLoader(avatarHref[0], loader1);

    Callback<Image, Void> loader2 = new Callback<Image, Void>() {
      @Override
      public Void call(Image arg0) {
        chooseAvarta2.setFill(new ImagePattern(arg0));
        return null;
      }
    };
    LoadLater.addLoader(avatarHref[1], loader2);

    Callback<Image, Void> loader3 = new Callback<Image, Void>() {
      @Override
      public Void call(Image arg0) {
        chooseAvarta3.setFill(new ImagePattern(arg0));
        return null;
      }
    };
    LoadLater.addLoader(avatarHref[2], loader3);

    Callback<Image, Void> loader4 = new Callback<Image, Void>() {
      @Override
      public Void call(Image arg0) {
        chooseAvarta4.setFill(new ImagePattern(arg0));
        return null;
      }
    };
    LoadLater.addLoader(avatarHref[3], loader4);

    // set default avatar
    handleSetAvatar1();

    // chooseAvatarButton1
    chooseAvatarButton1.setOnAction(_ -> {
      handleSetAvatar1();
    });

    // chooseAvatarButton2
    chooseAvatarButton2.setOnAction(_ -> {
      handleSetAvatar2();
    });

    // chooseAvatarButton3
    chooseAvatarButton3.setOnAction(_ -> {
      handleSetAvatar3();
    });

    // chooseAvatarButton4
    chooseAvatarButton4.setOnAction(_ -> {
      handleSetAvatar4();
    });

    // clear errors
    txtUsername.setOnKeyTyped(_ -> clearErrors());
    txtEmail.setOnKeyTyped(_ -> clearErrors());
    txtPassword.setOnKeyTyped(_ -> clearErrors());
    txtConfirmPassword.setOnKeyTyped(_ -> clearErrors());
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

    // confirm password
    String confirmPassword = txtConfirmPassword.getText();
    if (!password.equals(confirmPassword)) {
      actionMessage.setText("Password không khớp!");
      txtConfirmPassword.requestFocus();
      return;
    }

    // render avatar href
    String href = avatarHref[avatar - 1];

    // Start register
    System.out.println("Username: " + username);
    System.out.println("Password: " + password);
    // hash password
    password = AccountManager.hashPassword(password);
    if (AccountManager.register(username, email, password, href)) {
      actionMessage.setText("Đăng ký thành công!");
      App.getNotificationManager().notify("Đăng ký thành công!", NotificationManager.SUCCESS);

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

  private void clearErrors() {
    actionMessage.setText("");
  }
}
