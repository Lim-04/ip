package xiaozhi.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * A dialog box combining a speaker's avatar and a label of their message.
 * <p>
 * User messages keep the avatar on the right and the default (blue)
 * bubble style; {@link #getXiaoZhiDialog} mirrors the layout so XiaoZhi's
 * avatar appears on the left with the {@code reply-label} bubble style
 * instead, additionally tinted by the kind of command that produced it
 * (see {@link #changeDialogStyle(String)}).
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load DialogBox.fxml.", e);
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Mirrors this dialog box so the avatar appears on the left and the
     * text on the right, instead of the default right-aligned layout, and
     * switches its bubble to the {@code reply-label} style.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Tints this dialog box's bubble according to the kind of command that
     * produced it, e.g. green for a task being added. Commands with no
     * special styling (list, find, exit, ...) are left with the default
     * {@code reply-label} look.
     *
     * @param commandType Simple class name of the {@code Command} that produced this reply,
     *         e.g. {@code "AddCommand"}.
     */
    private void changeDialogStyle(String commandType) {
        switch (commandType) {
        case "AddCommand":
            dialog.getStyleClass().add("add-label");
            break;
        case "MarkCommand":
            dialog.getStyleClass().add("marked-label");
            break;
        case "DeleteCommand":
            dialog.getStyleClass().add("delete-label");
            break;
        default:
            // No special styling for this command type; keep the plain reply-label look.
            break;
        }
    }

    /**
     * Creates a dialog box for something the user said.
     *
     * @param text Text to display.
     * @param img Avatar to display.
     * @return The user's dialog box, avatar on the right.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Creates a dialog box for something XiaoZhi said.
     *
     * @param text Text to display.
     * @param img Avatar to display.
     * @param commandType Simple class name of the {@code Command} that produced this reply,
     *         used to tint the bubble (see {@link #changeDialogStyle(String)}).
     * @return XiaoZhi's dialog box, avatar on the left.
     */
    public static DialogBox getXiaoZhiDialog(String text, Image img, String commandType) {
        var db = new DialogBox(text, img);
        db.flip();
        db.changeDialogStyle(commandType);
        return db;
    }
}
