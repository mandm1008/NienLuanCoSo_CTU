module ui {
  requires transitive javafx.graphics;
  requires javafx.base;
  requires javafx.media;
  requires javafx.controls;
  requires javafx.fxml;

  requires google.api.client;
  requires com.google.api.client;
  requires com.google.api.services.drive;
  requires com.google.auth;
  requires com.google.api.client.json.gson;
  requires com.google.auth.oauth2;
  requires com.google.gson;

  requires java.sql;
  requires java.logging;
  requires java.net.http;
  requires java.desktop;
  requires java.xml;

  opens ui to javafx.fxml;
  opens ui.controllers to javafx.fxml;

  exports ui;
  exports ui.controllers;
}
