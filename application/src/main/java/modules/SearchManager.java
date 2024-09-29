package modules;

import java.util.HashMap;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;

import db.SongModel;
import ui.App;
import ui.DefindUI;

public class SearchManager {
  public static final KeyCode KEY_CODE = KeyCode.ENTER;
  private String keySearch;
  private LinkedList<SongModel> songs;
  private HashMap<String, Runnable> eventOnSearch;

  public SearchManager() {
    this.keySearch = "";
    this.eventOnSearch = new HashMap<>();
  }

  public SearchManager(String keySearch) {
    this.keySearch = keySearch;
  }

  public void setKeySearch(String keySearch) {
    this.keySearch = keySearch;
  }

  public String getKeySearch() {
    return keySearch;
  }

  public LinkedList<SongModel> getSongs() {
    return songs;
  }

  public void addEventOnSearch(String key, Runnable event) {
    eventOnSearch.put(key, event);
  }

  public void runEventOnSearch() {
    eventOnSearch.forEach((key, event) -> {
      event.run();
    });
  }

  public void setSongs(LinkedList<SongModel> songs) {
    this.songs = songs;
  }

  public void search(String keyS) {
    // handle search
    new Thread(() -> {
      setKeySearch(keyS);
      setSongs(SongModel.searchSongs(getKeySearch()));

      if (App.getCurrentContent() == DefindUI.getSearch()) {
        runEventOnSearch();
        return;
      }

      // update UI
      Platform.runLater(() -> {
        App.redirect(DefindUI.getSearch());
      });
    }).start();
  }
}
