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
      Parent content = DefindUI.loadFXML(DefindUI.getHome()).load();
      Parent footer = DefindUI.loadFXML(DefindUI.getFooter()).load();
      Parent menu = DefindUI.loadFXML(DefindUI.getMenu()).load();

      rootBorderPane.setTop(header);
      rootBorderPane.setCenter(content);
      rootBorderPane.setBottom(footer);
      rootBorderPane.setLeft(menu);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * Method set main content
   */
  @FXML
  public void setterPage(String content) throws IOException {
    Parent newContent = DefindUI.loadFXML(content).load();
    rootBorderPane.setCenter(newContent);
  }

  @FXML
  public void setterPage(String content, String header, String footer) throws IOException {
    Parent newContent = DefindUI.loadFXML(content).load();
    Parent newHeader = DefindUI.loadFXML(header).load();
    Parent newFooter = DefindUI.loadFXML(footer).load();

    rootBorderPane.setTop(newHeader);
    rootBorderPane.setCenter(newContent);
    rootBorderPane.setBottom(newFooter);
  }
}
