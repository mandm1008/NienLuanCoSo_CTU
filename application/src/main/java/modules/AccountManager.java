package modules;

import java.util.HashMap;

import db.UserModel;

public class AccountManager {
  private static final String DEFAULT_AVATAR = AccountManager.class.getResource("/images/avatar_default.png")
      .toExternalForm();

  private static int id = -1;
  private static String username = null;
  private static String email = null;
  private static String avatar = null;

  private static HashMap<String, Runnable> eventLogin = new HashMap<String, Runnable>();
  private static HashMap<String, Runnable> eventLogout = new HashMap<String, Runnable>();

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
    return avatar == null ? DEFAULT_AVATAR : avatar;
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

    // log id and username email avatar
    System.out.println("id: " + AccountManager.id);
    System.out.println("username: " + AccountManager.username);
    System.out.println("email: " + AccountManager.email);
    System.out.println("avatar: " + AccountManager.avatar);

    // run event login
    runEventLogin();

    return true;
  }

  public static void logout() {
    AccountManager.id = -1;
    AccountManager.username = null;
    AccountManager.email = null;
    AccountManager.avatar = null;

    // run event logout
    runEventLogout();
  }

  public static void likeSong(int songId) {
    if (AccountManager.id < 0) {
      return;
    }

    UserModel user = new UserModel(AccountManager.id);
    user.likeSong(songId);
  }

  public static void unlikeSong(int songId) {
    if (AccountManager.id < 0) {
      return;
    }

    UserModel user = new UserModel(AccountManager.id);
    user.unlikeSong(songId);
  }

  public static boolean checkLikedSong(int songId) {
    if (AccountManager.id < 0) {
      return false;
    }

    UserModel user = new UserModel(AccountManager.id);
    return user.checkLikedSong(songId);
  }

  public static void addEventLogin(String key, Runnable handler) {
    eventLogin.put(key, handler);
  }

  public static void addEventLogout(String key, Runnable handler) {
    eventLogout.put(key, handler);
  }

  private static void runEventLogin() {
    eventLogin.forEach((key, handler) -> {
      handler.run();
    });
  }

  private static void runEventLogout() {
    eventLogout.forEach((key, handler) -> {
      handler.run();
    });
  }
}
