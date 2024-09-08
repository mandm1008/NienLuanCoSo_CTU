package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import db.ConnectDB;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

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

        // try connect to database
        ConnectDB connectDB = new ConnectDB();
        connectDB.closeConnect();

        // just for testing
        redirect(DefindUI.getNoLayout(), DefindUI.getRegister());
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

    public static void main(String[] args) {
        launch();
    }

}