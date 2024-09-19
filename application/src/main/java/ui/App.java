package ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import modules.ImageManager;
import modules.MusicManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage primaryStage;
    private static Scene scene;
    private static MusicManager musicManager;
    private static String currentLayout;
    private static String currentContent;

    private StackPane loadingLayout;

    @Override
    public void start(Stage stage) throws IOException {
        // show loading screen
        FXMLLoader loadingLoader = DefindUI.loadFXML(DefindUI.getLoading());
        loadingLayout = loadingLoader.load();

        // set scene
        scene = new Scene(loadingLayout);
        scene.getStylesheets().add(App.class.getResource("/css/scene.css").toExternalForm());

        // set stage
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Your Music");
        stage.getIcons().add(new Image(App.class.getResource("/images/banner-solid.png").toExternalForm()));
        stage.show();

        new Thread(() -> {
            // init music media
            musicManager = new MusicManager();
            primaryStage = stage;

            // load images
            ImageManager.loadImages();

            try {
                // load layout
                BorderPane rootLayout = DefindUI.loadFXML(DefindUI.getLayout()).load();
                currentLayout = DefindUI.getLayout();

                // set main content
                Parent content = DefindUI.loadFXML(DefindUI.getHome()).load();
                currentContent = DefindUI.getHome();

                rootLayout.setCenter(content);

                // set scene
                Platform.runLater(() -> {
                    scene.setRoot(rootLayout);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void reload() {
        redirect(currentLayout, currentContent);
    }

    public static void redirect(String content) {
        redirect(DefindUI.getLayout(), content);
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

        currentLayout = layout;
        currentContent = content;
    }

    public static String getCurrentLayout() {
        return currentLayout;
    }

    public static String getCurrentContent() {
        return currentContent;
    }

    @SuppressWarnings("exports")
    public static MusicManager getMusicManager() {
        return musicManager;
    }

    public static void main(String[] args) {
        launch();
    }

}
