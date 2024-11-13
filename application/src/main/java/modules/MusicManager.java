package modules;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.application.Platform;
import ui.App;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import db.PlaylistModel;
import db.PlaylistSongModel;
import db.SettingModel;
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
  private ConcurrentHashMap<String, Runnable> eventOnChange = new ConcurrentHashMap<>();
  private HashMap<String, Runnable> eventOnPlay = new HashMap<>();
  private HashMap<String, Runnable> eventOnLoad = new HashMap<>();
  private HashMap<String, Runnable> eventOnChangePlaylist = new HashMap<>();
  private HashMap<String, Runnable> eventOnReady = new HashMap<>();

  // for state
  private boolean isPlaying = false;
  private double defaultVolume = 1.0;

  public MusicManager() {
    loadSetting();

    try {
      if (this.playlist.size() == 0)
        this.playlist = new LinkedList<SongModel>(SongModel.getNewSongs(9));
    } catch (Exception e) {
      this.playlist = Downloader.getDownloadedSongs();
    }

    this.index = 0;
    reLoadData();
  }

  public MusicManager(PlaylistModel playlistInfo) {
    this.playlistInfo = playlistInfo;

    try {
      if (playlistInfo.getUserId() == -127) {
        this.playlist = Downloader.getDownloadedSongs();
      } else {
        PlaylistSongModel psm = new PlaylistSongModel();
        this.playlist = psm.getSongsByPlaylistId(playlistInfo.getPlaylistId());
      }
    } catch (Exception e) {
      this.playlist = new LinkedList<SongModel>(SongModel.getNewSongs(9));
    }

    this.index = 0;
    reLoadData();
  }

  public void clearMusicManager() {
    if (this.mediaPlayer != null) {
      this.mediaPlayer.stop();
    }
  }

  private boolean loadSetting() {
    SettingModel setting = AccountManager.getSetting();
    if (setting == null) {
      return false;
    }

    // set playlist
    if (setting.getPlaylistId() == 0) {
      this.playlist = SongModel.getNewSongs(9);
    } else if (setting.getPlaylistId() == 1) {
      this.playlist = SongModel.getMostViewSongs(9);
    } else {
      PlaylistSongModel psm = new PlaylistSongModel();
      LinkedList<SongModel> songs = psm.getSongsByPlaylistId(setting.getPlaylistId());
      if (songs.size() > 0) {
        this.playlistInfo = setting.getPlaylist();
        this.playlist = songs;
      }
    }

    // set shuffle mode
    modeShuffle = setting.getShuff();

    // play now
    isPlaying = setting.getPlayNow();

    // set volume
    defaultVolume = setting.getVolume() / 100.0;

    return true;
  }

  private void setting() {
    // check REPEAT mode and SHUFFLE mode
    this.mediaPlayer.setOnEndOfMedia(() -> {
      if (modeRepeat) {
        this.mediaPlayer.seek(Duration.ZERO);
        this.mediaPlayer.play();
      } else {
        if (modeShuffle) {
          int newIndex;
          do {
            newIndex = (int) (Math.random() * playlist.size());
          } while (newIndex == this.index);
          changeMusic(newIndex);
        } else {
          forwardMusic();
        }
      }
    });

    this.mediaPlayer.setOnPlaying(() -> {
      runEventOnPlay();
    });

    this.mediaPlayer.setOnReady(() -> {
      runEventOnReady();

      if (isPlaying) {
        playMusic();
      }
    });

    this.mediaPlayer.setOnError(() -> {
      System.out.println("Error occurred: " + mediaPlayer.getError().getMessage());
      reLoadData();
    });

    this.mediaPlayer.setVolume(defaultVolume);
  }

  private void reLoadData() {
    // run event onLoad
    runEventOnLoad();

    // clear media player
    if (this.mediaPlayer != null)
      this.mediaPlayer.stop();

    // check youtube external
    String external = playlist.get(index).getExternal();
    Media media = null;
    if (external != null) {
      String href = YoutubeData.download(external).getHrefLocal();
      Path path = Path.of(href);
      media = new Media(path.toUri().toString());
    } else {
      // load new media
      System.out.println("Playing music link::: " + playlist.get(index).getHref());
      media = new Media(playlist.get(index).getHref());
    }

    this.mediaPlayer = new MediaPlayer(media);

    // reload setting
    setting();

    // update ui
    runEventOnChange();
  }

  public void setSong(SongModel song) {
    this.playlist.add(song);
    this.index = playlist.size() - 1;
    reLoadData();
  }

  public double getVolume() {
    return this.defaultVolume;
  }

  public void setVolume(double volume) {
    this.defaultVolume = volume;
    this.mediaPlayer.setVolume(volume);
  }

  public void setPlaylistInfo(PlaylistModel playlistInfo) {
    // load Playlist
    new Thread(() -> {
      LinkedList<SongModel> songs;

      if (playlistInfo.getUserId() == -127) {
        songs = Downloader.getDownloadedSongs();
      } else {
        PlaylistSongModel psm = new PlaylistSongModel();
        songs = psm.getSongsByPlaylistId(playlistInfo.getPlaylistId());
      }

      if (songs.size() == 0) {
        Platform.runLater(() -> {
          App.getNotificationManager().notify("Danh sách phát trống", NotificationManager.ERROR);
        });

        eventOnChange.get("playlist-menu-playlist-play").run();
        return;
      }

      // set playlist
      this.playlistInfo = playlistInfo;
      setPlaylist(songs);
    }).start();
  }

  public void setPlaylist(LinkedList<SongModel> playlist) {
    this.playlist.clear();
    this.playlist = playlist;
    this.index = 0;

    // events
    runEventOnChangePlaylist();
    reLoadData();
    playMusic();
  }

  public void addToPlaylist(SongModel song) {
    if (includeMusic(song)) {
      return;
    }

    this.playlist.add(song);

    // run event onChangePlaylist
    runEventOnChangePlaylist();
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

    this.index = index;
    reLoadData();
    System.out.println("Change music to: " + playlist.get(index).getTitle() + "and play");
    playMusic();

    // increase view
    new Thread(() -> {
      if (App.isInternet)
        playlist.get(index).increaseView();
    }).start();
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
    for (SongModel s : playlist) {
      if (s.getSongId() == song.getSongId()) {
        return true;
      }
    }

    return false;
  }

  public int findIndexOfSong(SongModel song) {
    for (int i = 0; i < playlist.size(); i++) {
      if (playlist.get(i).getSongId() == song.getSongId()) {
        return i;
      }
    }

    return -1;
  }

  public void removeMusic(int index) {
    if (index < 0 || index >= playlist.size()) {
      System.out.println("Index out of range");
      return;
    }

    this.playlist.remove(index);
    if (this.index == index) {
      if (index >= playlist.size()) {
        this.index = playlist.size() - 1;
      }
      reLoadData();
      playMusic();
    } else if (this.index > index) {
      this.index--;
    }

    // run event onChangePlaylist
    runEventOnChangePlaylist();
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
      this.isPlaying = false;
    } catch (Exception e) {
      System.out.println("No song to pause");
    }
  }

  public void prev10sMusic() {
    try {
      this.mediaPlayer.seek(this.mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
    } catch (Exception e) {
      System.out.println("No song to seek");
    }
  }

  public void next10sMusic() {
    try {
      this.mediaPlayer.seek(this.mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    } catch (Exception e) {
      System.out.println("No song to seek");
    }
  }

  public void playMusic() {
    try {
      this.mediaPlayer.play();
      this.isPlaying = true;
      System.out.println("Playing: " + playlist.get(index).getTitle());
      System.out.println("Href: " + playlist.get(index).getHref());
    } catch (Exception e) {
      System.out.println("No song to play");
    }
  }

  public void addEventOnChange(String key, Runnable runnable) {
    this.eventOnChange.put(key, runnable);
  }

  public void removeEventOnChange(String key) {
    this.eventOnChange.remove(key);
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

  public void addEventOnChangePlaylist(String key, Runnable runnable) {
    this.eventOnChangePlaylist.put(key, runnable);
  }

  public void runEventOnChangePlaylist() {
    // clear onChange key "playlist-item-*"
    Iterator<String> iterator = eventOnChange.keySet().iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      if (key.contains("playlist-item-")) {
        iterator.remove();
      }
    }

    for (String key : eventOnChangePlaylist.keySet()) {
      eventOnChangePlaylist.get(key).run();
      System.out.println("Run event onChangePlaylist: " + key);
    }
  }

  public void addEventOnReady(String key, Runnable runnable) {
    this.eventOnReady.put(key, runnable);
  }

  public void runEventOnReady() {
    for (String key : eventOnReady.keySet()) {
      eventOnReady.get(key).run();
      System.out.println("Run event onReady: " + key);
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
