package modules;

import db.UserModel;

public class AccountManager {
  private static int id;
  private static String username;
  private static String email;

  public static int getId() {
    return id;
  }

  public static String getUsername() {
    return username;
  }

  public static String getEmail() {
    return email;
  }

  public static boolean register(String username, String email, String password) {
    UserModel user = new UserModel(username, password, email);
    return user.insert();
  }

  public static boolean login(String username, String password) {
    // sql call
    UserModel user = UserModel.match(username, password);
    if (user == null) {
      return false;
    }

    AccountManager.id = user.getId();
    AccountManager.username = user.getUsername();
    AccountManager.email = user.getEmail();

    return true;
  }

  public static void logout() {
    AccountManager.id = 0;
    AccountManager.username = null;
    AccountManager.email = null;
  }
}
