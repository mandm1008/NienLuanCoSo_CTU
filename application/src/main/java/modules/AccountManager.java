package modules;

import java.util.HashMap;
import java.util.LinkedList;

import db.UserModel;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import ui.App;
import db.PlaylistModel;
import db.PlaylistSongModel;
import db.SongModel;

public class AccountManager {
  private static final String DEFAULT_AVATAR = AccountManager.class.getResource("/images/avatar_default.png")
      .toExternalForm();

  private static int id = -1;
  private static String username = null;
  private static String email = null;
  private static String avatar = null;

  private static HashMap<String, Runnable> eventLogin = new HashMap<String, Runnable>();
  private static HashMap<String, Runnable> eventLogout = new HashMap<String, Runnable>();
  private static HashMap<String, Runnable> eventChangePlaylist = new HashMap<String, Runnable>();

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

  public static void addEventChangePlaylist(String key, Runnable handler) {
    eventChangePlaylist.put(key, handler);
  }

  private static void runEventLogin() {
    eventLogin.forEach((key, handler) -> {
      handler.run();
      System.out.println("Run login event: " + key);
    });
  }

  private static void runEventLogout() {
    eventLogout.forEach((key, handler) -> {
      handler.run();
      System.out.println("Run logout event: " + key);
    });
  }

  public static void runEventChangePlaylist() {
    eventChangePlaylist.forEach((key, handler) -> {
      handler.run();
      System.out.println("Run change playlist event: " + key);
    });
  }

  public static LinkedList<PlaylistModel> getPlaylists() {
    if (AccountManager.id < 0) {
      return null;
    }

    PlaylistModel playlist = new PlaylistModel();
    return playlist.findByUserId(AccountManager.id);
  }

  public static void addToPlaylist(SongModel song, PlaylistModel playlist) {
    if (AccountManager.id < 0) {
      return;
    }

    PlaylistSongModel playlistSong = new PlaylistSongModel(playlist.getPlaylistId(), song.getSongId());
    if (playlistSong.insert()) {
      App.getNotificationManager().notify("Thêm '" + song.getTitle() + "' vào '" + playlist.getName() + "' thành công",
          NotificationManager.SUCCESS);
    } else {
      App.getNotificationManager().notify(
          "Thêm '" + song.getTitle() + "' vào '" + playlist.getName() + "' thất bại! Vui lòng thử lại",
          NotificationManager.ERROR);
    }
  }

  public static void addToPlaylist(SongModel song, double x, double y) {
    if (AccountManager.id < 0) {
      App.getMusicManager().addToPlaylist(song);
      return;
    }

    Popup popup = new Popup();
    popup.setAutoHide(true);
    popup.setX(x);
    popup.setY(y);
    LinkedList<PlaylistModel> playlists = AccountManager.getPlaylists();

    if (playlists == null || playlists.size() == 0) {
      App.getMusicManager().addToPlaylist(song);
      return;
    }

    VBox box = new VBox();
    box.setStyle("-fx-border-color: white;" +
        "fx-border-width: 2px;" +
        "fx-border-radius: 5px;" +
        "fx-background-color: #664E88;" +
        "fx-background-radius: 5px;" +
        " -fx-padding: 10px;");
    box.getStylesheets().add(AccountManager.class.getResource("/css/playlist.css").toExternalForm());
    box.setSpacing(8);
    box.setFillWidth(true);

    // add playlist on play
    Button defaultPlayButton = new Button("Danh sách đang phát");
    defaultPlayButton.getStyleClass().add("clear-button");
    defaultPlayButton.getStyleClass().add("playlist-hover");
    defaultPlayButton.setStyle("-fx-text-fill: white; -fx-text-alignment: left;");
    defaultPlayButton.setMaxWidth(Double.MAX_VALUE);
    defaultPlayButton.setOnAction(e -> {
      App.getMusicManager().addToPlaylist(song);
      popup.hide();
    });
    box.getChildren().add(defaultPlayButton);

    // add playlist item
    for (PlaylistModel playlist : playlists) {
      Button button = new Button(playlist.getName());
      button.getStyleClass().add("clear-button");
      button.getStyleClass().add("playlist-hover");
      button.setStyle("-fx-text-fill: white; -fx-text-alignment: left;");
      button.setMaxWidth(Double.MAX_VALUE);
      button.setOnAction(e -> {
        AccountManager.addToPlaylist(song, playlist);
        popup.hide();
      });
      box.getChildren().add(button);
    }

    popup.getContent().add(box);
    popup.show(App.getPrimaryStage());
  }

  public static boolean createPlaylist(String name) {
    if (AccountManager.id < 0) {
      return false;
    }

    PlaylistModel playlist = new PlaylistModel(name, AccountManager.id);
    if (playlist.insert()) {
      runEventChangePlaylist();
      return true;
    } else {
      return false;
    }
  }

  public static boolean removePlaylist(PlaylistModel playlist) {
    if (AccountManager.id < 0) {
      return false;
    }

    if (playlist.delete()) {
      runEventChangePlaylist();
      return true;
    } else {
      return false;
    }
  }

  public static LinkedList<SongModel> getSongs() {
    if (AccountManager.id < 0) {
      return null;
    }

    SongModel song = new SongModel();
    return song.getSongsByUserId(id);
  }
}
