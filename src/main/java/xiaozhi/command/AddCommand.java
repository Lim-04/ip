package xiaozhi.command;

import xiaozhi.storage.Storage;
import xiaozhi.task.Task;
import xiaozhi.task.TaskList;
import xiaozhi.ui.Ui;

public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        ui.showAdded(task, tasks.size());
        storage.save(tasks.asList());
    }
}
