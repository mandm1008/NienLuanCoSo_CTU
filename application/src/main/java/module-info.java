module ui {
    requires transitive javafx.graphics;
    requires javafx.media;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens ui to javafx.fxml;
    opens ui.controllers to javafx.fxml;

    exports ui;
    exports ui.controllers;
}
