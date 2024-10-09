module ui {
  requires transitive javafx.graphics;
  requires javafx.base;
  requires javafx.media;
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;

  requires google.api.client;
  requires com.google.api.client;
  requires com.google.api.services.drive;
  requires com.google.auth;
  requires com.google.api.client.json.gson;
  requires com.google.auth.oauth2;

  requires java.logging;
  requires java.net.http;
  requires java.desktop;
  requires java.xml;
  requires com.google.gson;

  opens ui to javafx.fxml;
  opens ui.controllers to javafx.fxml;

  exports ui;
  exports ui.controllers;
}
