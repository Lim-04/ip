package xiaozhi;

import xiaozhi.command.Command;
import xiaozhi.exception.XiaoZhiException;
import xiaozhi.parser.Parser;
import xiaozhi.storage.Storage;
import xiaozhi.task.TaskList;
import xiaozhi.ui.Ui;

public class XiaoZhi {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public XiaoZhi(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList(storage.load());
    }

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

    public static void main(String[] args) {
        new XiaoZhi("./data/xiaozhi.txt").run();
    }
}
