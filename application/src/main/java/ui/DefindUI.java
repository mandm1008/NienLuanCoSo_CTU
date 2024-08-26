package ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class DefindUI {
  private static String layout = "layout";
  private static String home = "home";
  private static String header = "header";
  private static String footer = "footer";

  public static String getLayout() {
    return layout;
  }

  public static String getHome() {
    return home;
  }

  public static String getHeader() {
    return header;
  }

  public static String getFooter() {
    return footer;
  }

  @SuppressWarnings("exports")
  public static FXMLLoader loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(DefindUI.class.getResource(fxml + ".fxml"));
    return fxmlLoader;
  }
}
