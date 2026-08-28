package xiaozhi;

import xiaozhi.command.Command;
import xiaozhi.exception.XiaoZhiException;
import xiaozhi.parser.Parser;
import xiaozhi.storage.Storage;
import xiaozhi.task.TaskList;
import xiaozhi.ui.Ui;

/**
 * Entry point for the XiaoZhi chatbot.
 * <p>
 * Wires together a {@link Ui}, a {@link Storage} and a {@link TaskList},
 * then drives the read-parse-execute loop that turns each line of input
 * into a {@link Command} via {@link Parser} and runs it.
 */
public class XiaoZhi {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a XiaoZhi that persists its tasks to the given file path.
     *
     * @param filePath Path to the save file, passed straight to {@link Storage}.
     */
    public XiaoZhi(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList(storage.load());
    }

    /**
     * Runs the chatbot until the user issues an exit command.
     * Prints the banner and greeting first, then repeatedly reads a
     * command, parses and executes it, and reports any error without
     * stopping the loop, until a command signals it is time to exit.
     */
    public void run() {
        ui.showBanner();
        ui.showGreeting();
        boolean isExit = false;
        String input = ui.readCommand();

        while (!isExit) {
            try {
                Command command = Parser.parse(input);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (XiaoZhiException e) {
                ui.showError(e.getMessage());
            }

            if (!isExit) {
                input = ui.readCommand();
            }
        }

        ui.showFarewell();
    }

    /**
     * Starts XiaoZhi, saving tasks to {@code ./data/xiaozhi.txt}.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new XiaoZhi("./data/xiaozhi.txt").run();
    }
}
