package ui.controllers;

import db.SongModel;

public abstract class MenuMusic {
  // @SuppressWarnings("exports")
  public abstract void setSong(SongModel songData);

  public abstract void setOnActionEvent(Runnable runner);
}
