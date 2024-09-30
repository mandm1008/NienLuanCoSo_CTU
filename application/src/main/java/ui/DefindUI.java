package ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class DefindUI {
  private static final String layout = "layout";
  private static final String noLayout = "no_layout";
  private static final String home = "home";
  private static final String header = "header";
  private static final String player = "player";
  private static final String menu = "menu";
  private static final String login = "login";
  private static final String register = "register";
  private static final String playlist = "playlist";
  private static final String playlistItem = "playlist-item";
  private static final String loading = "loading";
  private static final String musicItem = "music-item";
  private static final String search = "search";
  private static final String musicMenu = "music-menu";

  public static String getLayout() {
    return layout;
  }

  public static String getNoLayout() {
    return noLayout;
  }

  public static String getHome() {
    return home;
  }

  public static String getHeader() {
    return header;
  }

  public static String getPlayer() {
    return player;
  }

  public static String getMenu() {
    return menu;
  }

  public static String getLogin() {
    return login;
  }

  public static String getRegister() {
    return register;
  }

  public static String getPlaylist() {
    return playlist;
  }

  public static String getPlaylistItem() {
    return playlistItem;
  }

  public static String getLoading() {
    return loading;
  }

  public static String getMusicItem() {
    return musicItem;
  }

  public static String getSearch() {
    return search;
  }

  public static String getMusicMenu() {
    return musicMenu;
  }

  @SuppressWarnings("exports")
  public static FXMLLoader loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(DefindUI.class.getResource(fxml + ".fxml"));
    return fxmlLoader;
  }
}
