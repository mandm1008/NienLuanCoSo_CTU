package ui;

import java.io.IOException;

import javafx.scene.Parent;

public class LayoutCache {
  private Parent header;
  private Parent menu;
  private Parent player;

  public void load() {
    try {
      header = DefindUI.loadFXML(DefindUI.getHeader()).load();
      menu = DefindUI.loadFXML(DefindUI.getMenu()).load();
      player = DefindUI.loadFXML(DefindUI.getPlayer()).load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Parent getHeader() {
    return header;
  }

  public Parent getMenu() {
    return menu;
  }

  public Parent getPlayer() {
    return player;
  }
}
