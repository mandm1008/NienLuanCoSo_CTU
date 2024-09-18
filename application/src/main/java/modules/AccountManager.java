package modules;

import db.UserModel;

public class AccountManager {
  private static int id = -1;
  private static String username = null;
  private static String email = null;
  private static String avatar = null;

  public static int getId() {
    return id;
  }

  public static String getUsername() {
    return username;
  }

  public static String getEmail() {
    return email;
  }

  public static String getAvatar() {
    return avatar;
  }

  public static boolean register(String username, String email, String password, String avatar) {
    UserModel user = new UserModel(username, password, email, avatar);
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
    AccountManager.avatar = user.getAvatar();

    return true;
  }

  public static void logout() {
    AccountManager.id = -1;
    AccountManager.username = null;
    AccountManager.email = null;
    AccountManager.avatar = null;
  }
}
