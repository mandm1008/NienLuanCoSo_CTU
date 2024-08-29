module ui {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens ui to javafx.fxml;

    exports ui;
}
