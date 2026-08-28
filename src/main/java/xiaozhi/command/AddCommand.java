package xiaozhi.command;

import xiaozhi.storage.Storage;
import xiaozhi.task.Task;
import xiaozhi.task.TaskList;
import xiaozhi.ui.Ui;

/**
 * Adds a single {@link Task} to the task list.
 * Used for {@code todo}, {@code deadline} and {@code event} commands alike,
 * since all three only differ in which Task subtype was already constructed.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates an AddCommand that will add the given task when executed.
     *
     * @param task The task to add.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds this command's task to {@code tasks}, reports it through
     * {@code ui}, and saves the updated list through {@code storage}.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        ui.showAdded(task, tasks.size());
        storage.save(tasks.asList());
    }
}
