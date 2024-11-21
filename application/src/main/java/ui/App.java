package ui;

import java.io.IOException;
import java.util.HashMap;

import db.ConnectDB;
import db.PlaylistModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import modules.AccountManager;
import modules.ImageManager;
import modules.MusicManager;
import modules.NetworkCheck;
import modules.NotificationManager;
import modules.SearchManager;
import modules.ThreadCustom;
import modules.YoutubeDownloader;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage primaryStage;
    private static Scene scene;
    private static LayoutCache layoutCache;
    private static String currentLayout;
    private static String currentContent;

    private static MusicManager musicManager;
    private static SearchManager searchManager;
    private static NotificationManager notificationManager;

    private static HashMap<String, Runnable> eventChangePage = new HashMap<>();
    private static HashMap<String, Runnable> eventReloadAll = new HashMap<>();

    // for internet
    public static boolean isInternet = true;
    private static final int ONLINE_MODE = 0;
    private static final int OFFLINE_MODE = 1;
    private static int currentMode = ONLINE_MODE; // 0: online, 1: offline
    private static Alert alertOnInternet;
    private static Alert alertNoInternet;

    @Override
    public void start(Stage stage) throws IOException {
        // show loading screen
        StackPane stackLayout = new StackPane();
        ProgressIndicator progressIndicator = new ProgressIndicator(-1.0);
        stackLayout.getChildren().add(progressIndicator);

        // set scene
        scene = new Scene(stackLayout);
        scene.getStylesheets().add(App.class.getResource("/css/scene.css").toExternalForm());

        // set stage
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Your Music");
        stage.getIcons().add(new Image(App.class.getResource("/images/banner-solid.png").toExternalForm()));
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(stage);
            alert.setTitle("Thoát Your Music");
            alert.setHeaderText("Bạn có chắc chắn muốn thoát ứng dụng không?");
            alert.setContentText("Nhấn OK để thoát, BYE BYE! (^.^)");

            // css
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // clean youtube data
                YoutubeDownloader.cleanAll();

                // stop all scheduled
                ThreadCustom.stopAll();

                // exit all stage
                Platform.exit();

                // exit application
                System.exit(0);
            } else {
                event.consume();
            }
        });

        loadAll(stage, scene);

        stage.show();

        NetworkCheck networkCheck = new NetworkCheck();
        networkCheck.setRunOnInternet(() -> {
            if (!NetworkCheck.check()) {
                isInternet = false;
                return;
            }

            if (alertNoInternet != null)
                alertNoInternet.close();

            if (currentMode != ONLINE_MODE && isSwitchToOnline(stage)) {
                Platform.runLater(() -> {
                    try {
                        loadAll(stage, scene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        networkCheck.setRunOnNoInternet(() -> {
            if (NetworkCheck.check()) {
                isInternet = true;
                return;
            }

            if (alertOnInternet != null)
                alertOnInternet.close();

            if (currentMode != OFFLINE_MODE && isSwitchToOffline(stage)) {
                Platform.runLater(() -> {
                    try {
                        loadAll(stage, scene);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        networkCheck.start();
    }

    private static boolean isSwitchToOnline(Stage st) {
        alertOnInternet = new Alert(AlertType.CONFIRMATION);
        alertOnInternet.initOwner(st);
        alertOnInternet.setTitle("Chuyển sang chế độ Online");
        alertOnInternet.setHeaderText("Đã có kết nối Internet");
        alertOnInternet.setContentText("Bạn có muốn chuyển sang Online không?");

        // css
        alertOnInternet.getDialogPane().getStylesheets().add(App.class.getResource("/css/alert.css").toExternalForm());

        return alertOnInternet.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private static boolean isSwitchToOffline(Stage st) {
        alertNoInternet = new Alert(AlertType.INFORMATION);
        alertNoInternet.initOwner(st);
        alertNoInternet.setTitle("Chuyển sang chế độ Offline");
        alertNoInternet.setHeaderText("Mất kết nối Internet");
        alertNoInternet.setContentText("Bạn có muốn chuyển sang Offline không?");

        // css
        alertNoInternet.getDialogPane().getStylesheets().add(App.class.getResource("/css/alert.css").toExternalForm());

        alertNoInternet.getButtonTypes().setAll(ButtonType.OK);

        return alertNoInternet.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.CANCEL;
    }

    public static void loadAll(Stage stage, Scene scene) throws IOException {
        // show loading screen
        StackPane stackLayout = new StackPane();
        ProgressIndicator progressIndicator = new ProgressIndicator(-1.0);
        stackLayout.getChildren().add(progressIndicator);

        // set scene
        scene.setRoot(stackLayout);

        new Thread(() -> {
            isInternet = NetworkCheck.check();

            // set mode
            if (isInternet) {
                currentMode = ONLINE_MODE;
            } else {
                currentMode = OFFLINE_MODE;
            }

            // title
            if (isInternet) {
                Platform.runLater(() -> {
                    stage.setTitle("Your Music");
                });
            } else {
                Platform.runLater(() -> {
                    stage.setTitle("Your Music (Offline)");
                });
            }

            // load properties
            boolean isGlobalDB = ConnectDB.loadProperties();

            // auto login
            System.out.println("Auto login");
            if (isInternet) {
                AccountManager.autoLogin();
            } else {
                AccountManager.clear();
            }

            // init music media
            System.out.println("Init music media");
            if (musicManager != null) {
                musicManager.clearMusicManager();
            }
            if (isInternet) {
                musicManager = new MusicManager();
            } else {
                musicManager = new MusicManager(new PlaylistModel("Đã tải", -127));
            }

            primaryStage = stage;

            // load images
            System.out.println("Load images");
            ImageManager.loadImages();

            // load layout
            System.out.println("Load layout cache");
            layoutCache = new LayoutCache();
            layoutCache.load();

            // load search manager
            System.out.println("Load search manager");
            searchManager = new SearchManager();

            // load notification manager
            System.out.println("Load notification manager");
            notificationManager = new NotificationManager(primaryStage);

            if (!isInternet) {
                notificationManager.notify("Không có kết nối internet, Ứng dụng sẽ chạy ở chế độ offline",
                        NotificationManager.WARNING);
            }

            if (isInternet && !isGlobalDB) {
                notificationManager.notify("Ứng dụng đang dùng cơ sở dữ liệu cục bộ! Vui lòng kiểm tra file cấu hình.",
                        NotificationManager.WARNING);
            }

            try {
                // load layout
                BorderPane rootLayout = DefindUI.loadFXML(DefindUI.getLayout()).load();
                currentLayout = DefindUI.getLayout();
                System.out.println("Load layout");

                // set main content
                Parent content = DefindUI.loadFXML(DefindUI.getHome()).load();
                currentContent = DefindUI.getHome();

                rootLayout.setCenter(content);

                // set scene
                Platform.runLater(() -> {
                    System.out.println("Set scene");
                    scene.setRoot(rootLayout);
                    runEventChangePage();
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

    public static LayoutCache getLayoutCache() {
        return layoutCache;
    }

    public static void reload() {
        new Thread(() -> {
            layoutCache.reload();

            Platform.runLater(() -> {
                redirect(currentLayout, currentContent);

                runEventReloadAll();
            });
        }).start();
    }

    public static void reloadPage() {
        Platform.runLater(() -> {
            redirect(currentLayout, currentContent);
        });
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
        runEventChangePage();
    }

    public static String getCurrentLayout() {
        return currentLayout;
    }

    public static String getCurrentContent() {
        return currentContent;
    }

    public static MusicManager getMusicManager() {
        return musicManager;
    }

    public static SearchManager getSearchManager() {
        return searchManager;
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static void addEventChangePage(String key, Runnable event) {
        eventChangePage.put(key, event);
    }

    public static void runEventChangePage() {
        eventChangePage.forEach((key, event) -> {
            System.out.println("Run change page event: " + key);
            event.run();
        });
    }

    public static void addEventReloadAll(String key, Runnable event) {
        eventReloadAll.put(key, event);
    }

    public static void runEventReloadAll() {
        eventReloadAll.forEach((key, event) -> {
            System.out.println("Run reload all event: " + key);
            event.run();
        });
    }

    public static void main(String[] args) {
        launch();
    }

}
