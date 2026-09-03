package xiaozhi.gui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import xiaozhi.XiaoZhi;

/**
 * Controller for the main GUI window.
 * <p>
 * Displays a scrolling history of dialog boxes above a text field and send
 * button, and forwards each line the user submits to the {@link XiaoZhi}
 * instance injected via {@link #setXiaoZhi(XiaoZhi)}.
 */
public class MainWindow extends AnchorPane {
    private static final Duration EXIT_DELAY = Duration.seconds(1.2);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private XiaoZhi xiaoZhi;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private final Image xiaoZhiImage = new Image(this.getClass().getResourceAsStream("/images/XiaoZhi.png"));

    /**
     * Keeps the dialog history scrolled to the newest message as it grows.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the XiaoZhi instance this window forwards user input to.
     *
     * @param xiaoZhi The XiaoZhi instance to talk to.
     */
    public void setXiaoZhi(XiaoZhi xiaoZhi) {
        this.xiaoZhi = xiaoZhi;
    }

    /**
     * Creates two dialog boxes, one echoing the user's input and the other
     * containing XiaoZhi's reply, appends them to the dialog container, then
     * clears the input field. If the input was an exit command, closes the
     * window shortly after showing the farewell message.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        String response = xiaoZhi.getResponse(input);
        String commandType = xiaoZhi.getCommandType();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getXiaoZhiDialog(response, xiaoZhiImage, commandType)
        );
        userInput.clear();

        if (xiaoZhi.isExit()) {
            PauseTransition delay = new PauseTransition(EXIT_DELAY);
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
