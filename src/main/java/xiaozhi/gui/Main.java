package xiaozhi.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import xiaozhi.XiaoZhi;

/**
 * A JavaFX GUI for {@link XiaoZhi}, loaded from {@code /view/MainWindow.fxml}.
 */
public class Main extends Application {
    private static final String SAVE_FILE_PATH = "./data/xiaozhi.txt";

    private final XiaoZhi xiaoZhi = new XiaoZhi(SAVE_FILE_PATH);

    /**
     * Builds and shows the main window, injecting the {@link XiaoZhi} instance
     * this app was started with into its controller.
     *
     * @param stage The primary stage provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.setTitle("XiaoZhi");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/XiaoZhi.png")));
            fxmlLoader.<MainWindow>getController().setXiaoZhi(xiaoZhi);
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load the GUI layout.", e);
        }
    }
}
