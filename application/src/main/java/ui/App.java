package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// import db.ConnectDB;
import modules.MusicManager;
import db.SongModel;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static MusicManager musicManager;

    @Override
    public void start(Stage stage) throws IOException {
        // load layout
        BorderPane rootLayout = DefindUI.loadFXML(DefindUI.getLayout()).load();

        // set main content
        Parent content = DefindUI.loadFXML(DefindUI.getHome()).load();
        rootLayout.setCenter(content);

        // set scene
        scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        // init music media
        SongModel song = new SongModel("Anh đếch cần gì nhiều ngoài em", 0, 0, 0,
                "https://dl165.filemate2.shop/?file=M3R4SUNiN3JsOHJ6WWQ2a3NQS1Y5ZGlxVlZIOCtyaGh1NEYvbmtBbUQrQUhrcDlxOHJYbEFlc0tkL1lCaHI3bk1vMFIxQjNWT3RhZmV5SzlnTk1rUTMyRnZQc0w5ekxmb01nbVVNVjBhUWI1ais3KzAyTlFrQWpnYXMzUEVQMEVTM3ByOEFVeDlTVFdudGZqcWxqSnYyMmVxMW1STkdvSG9taE5PdkRmcktsbWsyelRmLzcyMW9RTW9DUENzODBiaktETjVGU25rdlF0c2Nad0F4QWtLOElFamMrenpPYVJvVVZJMFE9PQ%3D%3D");
        musicManager = new MusicManager(song);

        // try connect to database
        // ConnectDB connectDB = new ConnectDB();
        // connectDB.closeConnect();

        // just for testing
        // redirect(DefindUI.getNoLayout(), DefindUI.getRegister());

        musicManager.playMusic();
    }

    public static void redirect(String content) {
        try {
            // load new page
            BorderPane rootLayout = DefindUI.loadFXML(DefindUI.getLayout()).load();
            Parent main = DefindUI.loadFXML(content).load();
            rootLayout.setCenter(main);

            // set new page
            scene.setRoot(rootLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void redirect(String layout, String content) {
        try {
            // load new page
            if (layout.equals(DefindUI.getNoLayout())) {
                Parent main = DefindUI.loadFXML(content).load();

                // wrapper center components
                StackPane sPane = new StackPane();
                sPane.getChildren().add(main);
                sPane.setStyle("-fx-padding: 40px");
                sPane.setStyle("-fx-background-color: #2C2F33");

                scene.setRoot(sPane);
            } else {
                BorderPane rootLayout = DefindUI.loadFXML(layout).load();
                Parent main = DefindUI.loadFXML(content).load();
                rootLayout.setCenter(main);

                scene.setRoot(rootLayout);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("exports")
    public static MusicManager getMusicManager() {
        return musicManager;
    }

    public static void main(String[] args) {
        launch();
    }

}
