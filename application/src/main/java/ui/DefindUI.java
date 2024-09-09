package ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class DefindUI {
  private static String layout = "layout";
  private static String noLayout = "no_layout";
  private static String home = "home";
  private static String header = "header";
  private static String player = "player";
  private static String menu = "menu";
  private static String login = "login";
  private static String register = "register";

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

  @SuppressWarnings("exports")
  public static FXMLLoader loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(DefindUI.class.getResource(fxml + ".fxml"));
    return fxmlLoader;
  }
}
