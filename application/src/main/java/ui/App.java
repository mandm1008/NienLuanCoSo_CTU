package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static FXMLLoader rootLayout;

    @Override
    public void start(Stage stage) throws IOException {
        rootLayout = DefindUI.loadFXML(DefindUI.getLayout());
        scene = new Scene(rootLayout.load(), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        rootLayout = DefindUI.loadFXML(fxml);
        scene.setRoot(rootLayout.load());
    }

    static void setContent(String content) throws IOException {
        LayoutController layoutController = rootLayout.getController();
        layoutController.setterPage(content);
    }

    static void setContent(String content, String header, String footer) throws IOException {
        LayoutController layoutController = rootLayout.getController();
        layoutController.setterPage(content, header, footer);
    }

    public static void main(String[] args) {
        launch();
    }

}