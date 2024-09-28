package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import ui.App;

public class LayoutController {
  @FXML
  private BorderPane rootBorderPane;

  @FXML
  public void initialize() {
    Parent header = App.getLayoutCache().getHeader();
    Parent footer = App.getLayoutCache().getPlayer();
    Parent menu = App.getLayoutCache().getMenu();

    rootBorderPane.setTop(header);
    rootBorderPane.setBottom(footer);
    rootBorderPane.setLeft(menu);
  }

}
