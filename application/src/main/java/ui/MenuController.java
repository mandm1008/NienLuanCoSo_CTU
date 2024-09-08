package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

  @FXML
  private Button exploreButton;

  @FXML
  private Button suggestButton;

  @FXML
  private Button topChartsButton;

  @FXML
  private Button newStuffButton;

  @FXML
  private Button favoriteButton;

  @FXML
  private Button albumsButton;

  @FXML
  private Button playlistsButton;

  @FXML
  private Button genresButton;

  @FXML
  private Button createPlaylistButton;

  @FXML
  public void initialize() {
    // Initialize logic or event handling for buttons here if needed
  }

  // Add methods to handle button actions
  public void onExploreClicked() {
    System.out.println("Explore clicked");
  }

  public void onSuggestClicked() {
    System.out.println("Suggest clicked");
  }
}
