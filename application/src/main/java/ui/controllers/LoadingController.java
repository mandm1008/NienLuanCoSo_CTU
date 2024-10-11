package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class LoadingController {
  @FXML
  private StackPane stackPane;
  @FXML
  private Label status;

  public void setBackgroundColor(String color) {
    this.stackPane.setStyle("-fx-background-color: " + color);
  }

  public void setStatus(String status) {
    this.status.setText(status);
  }
}
