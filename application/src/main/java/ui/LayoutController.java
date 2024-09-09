package ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class LayoutController {

  @FXML
  private BorderPane rootBorderPane;

  @FXML
  public void initialize() {
    try {
      // load the header, content, and footer from the FXML files
      Parent header = DefindUI.loadFXML(DefindUI.getHeader()).load();
      // Parent content = DefindUI.loadFXML(DefindUI.getHome()).load();
      Parent footer = DefindUI.loadFXML(DefindUI.getPlayer()).load();
      Parent menu = DefindUI.loadFXML(DefindUI.getMenu()).load();

      rootBorderPane.setTop(header);
      // rootBorderPane.setCenter(content);
      rootBorderPane.setBottom(footer);
      rootBorderPane.setLeft(menu);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
