package modules;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.LinkedList;

import db.PlaylistModel;
import db.SongModel;

public class MusicManager {
  // root data
  private MediaPlayer mediaPlayer;
  private PlaylistModel playlistInfo = new PlaylistModel("default", -1);
  private LinkedList<SongModel> playlist = new LinkedList<>();
  private int index = 0;

  // for mode
  private boolean modeRepeat = false;
  private boolean modeShuffle = false;

  // for event
  private HashMap<String, Runnable> eventOnChange = new HashMap<>();
  private HashMap<String, Runnable> eventOnPlay = new HashMap<>();
  private HashMap<String, Runnable> eventOnLoad = new HashMap<>();

  public MusicManager() {
    this.playlist = SongModel.getNewSongs(5);
    Media media = new Media(playlist.get(index).getHref());
    this.mediaPlayer = new MediaPlayer(media);
    setting();
  }

  public MusicManager(SongModel song) {
    setSong(song);
  }

  public MusicManager(LinkedList<SongModel> playlist) {
    this.playlist = playlist;
    this.index = 0;
    reLoadData();
  }

  private void setting() {
    // check REPEAT mode and SHUFFLE mode
    this.mediaPlayer.setOnEndOfMedia(() -> {
      if (modeRepeat) {
        this.mediaPlayer.seek(Duration.ZERO);
        this.mediaPlayer.play();
      } else if (modeShuffle) {
        int newIndex = (int) (Math.random() * playlist.size());
        changeMusic(newIndex);
      } else {
        forwardMusic();
      }
    });

    this.mediaPlayer.setOnPlaying(() -> {
      runEventOnPlay();
    });
  }

  private void reLoadData() {
    // run event onLoad
    runEventOnLoad();

    // clear media player
    if (this.mediaPlayer != null)
      this.mediaPlayer.stop();

    // load new media
    Media media = new Media(playlist.get(index).getHref());
    this.mediaPlayer = new MediaPlayer(media);

    // reload setting
    setting();

    // increase view
    playlist.get(index).increaseView();

    // update ui
    runEventOnChange();
  }

  public void setSong(SongModel song) {
    this.playlist.add(song);
    this.index = playlist.size() - 1;
    reLoadData();
  }

  public void setPlaylist(LinkedList<SongModel> playlist) {
    this.playlist.clear();
    this.playlist = playlist;
    this.index = 0;
    reLoadData();
  }

  public void addToPlaylist(SongModel song) {
    this.playlist.add(song);
  }

  public PlaylistModel getPlaylistInfo() {
    return this.playlistInfo;
  }

  public SongModel getCurrentSong() {
    return this.playlist.get(index);
  }

  public int getIndex() {
    return this.index;
  }

  public LinkedList<SongModel> getPlaylist() {
    return this.playlist;
  }

  public MediaPlayer getMediaPlayer() {
    return this.mediaPlayer;
  }

  public void changeMusic(int index) {
    if (index < 0 || index >= playlist.size()) {
      return;
    }

    if (this.index == index)
      return;

    this.index = index;
    reLoadData();
    playMusic();
  }

  public void changeMusic(SongModel song) {
    if (includeMusic(song)) {
      this.index = findIndexOfSong(song);
    } else {
      addToPlaylist(song);
      this.index = playlist.size() - 1;
    }

    // reload data
    reLoadData();
    playMusic();
  }

  public boolean includeMusic(SongModel song) {
    return this.playlist.contains(song);
  }

  public int findIndexOfSong(SongModel song) {
    return this.playlist.indexOf(song);
  }

  public void forwardMusic() {
    // check shuffle mode
    if (modeShuffle) {
      int newIndex = (int) (Math.random() * playlist.size());
      changeMusic(newIndex);
      return;
    }

    if (index < playlist.size() - 1) {
      changeMusic(index + 1);
    }
  }

  public void backMusic() {
    // check shuffle mode
    if (modeShuffle) {
      int newIndex = (int) (Math.random() * playlist.size());
      changeMusic(newIndex);
      return;
    }

    if (index > 0) {
      changeMusic(index - 1);
    }
  }

  public void pauseMusic() {
    try {
      this.mediaPlayer.pause();
    } catch (NullPointerException e) {
      System.out.println("No song to pause");
    }
  }

  public void prev10sMusic() {
    try {
      this.mediaPlayer.seek(this.mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
    } catch (NullPointerException e) {
      System.out.println("No song to seek");
    }
  }

  public void next10sMusic() {
    try {
      this.mediaPlayer.seek(this.mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    } catch (NullPointerException e) {
      System.out.println("No song to seek");
    }
  }

  public void playMusic() {
    try {
      this.mediaPlayer.play();
      System.out.println("Playing: " + playlist.get(index).getTitle());
      System.out.println("Href: " + playlist.get(index).getHref());
    } catch (NullPointerException e) {
      System.out.println("No song to play");
    }
  }

  public void addEventOnChange(String key, Runnable runnable) {
    this.eventOnChange.put(key, runnable);
  }

  private void runEventOnChange() {
    for (String key : eventOnChange.keySet()) {
      eventOnChange.get(key).run();
      System.out.println("Run event onChange: " + key);
    }
  }

  public void addEventOnPlay(String key, Runnable runnable) {
    this.eventOnPlay.put(key, runnable);
  }

  public void runEventOnPlay() {
    for (String key : eventOnPlay.keySet()) {
      eventOnPlay.get(key).run();
      System.out.println("Run event onPlay: " + key);
    }
  }

  public void addEventOnLoad(String key, Runnable runnable) {
    this.eventOnLoad.put(key, runnable);
  }

  public void runEventOnLoad() {
    for (String key : eventOnLoad.keySet()) {
      eventOnLoad.get(key).run();
      System.out.println("Run event onLoad: " + key);
    }
  }

  public void setRepeat(boolean modeRepeat) {
    this.modeRepeat = modeRepeat;
    setting();
  }

  public boolean isRepeat() {
    return this.modeRepeat;
  }

  public void setShuffle(boolean modeShuffle) {
    this.modeShuffle = modeShuffle;
    setting();
  }

  public boolean isShuffle() {
    return this.modeShuffle;
  }
}
