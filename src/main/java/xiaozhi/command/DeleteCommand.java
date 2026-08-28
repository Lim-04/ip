package xiaozhi.command;

import xiaozhi.exception.XiaoZhiException;
import xiaozhi.storage.Storage;
import xiaozhi.task.Task;
import xiaozhi.task.TaskList;
import xiaozhi.ui.Ui;

/**
 * Removes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int targetIndex;

    /**
     * Creates a DeleteCommand for the task at the given position.
     *
     * @param targetIndex 0-based index, within the task list, of the task to delete.
     */
    public DeleteCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Removes the target task, reports it through {@code ui}, and saves
     * the updated list through {@code storage}.
     *
     * @throws XiaoZhiException If {@code targetIndex} is not a valid position in {@code tasks}.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws XiaoZhiException {
        if (targetIndex < 0 || targetIndex >= tasks.size()) {
            throw new XiaoZhiException(
                    "Task " + (targetIndex + 1) + " doesn't exist. You have " + tasks.size() + " task(s).");
        }
        Task removedTask = tasks.remove(targetIndex);
        ui.showRemoved(removedTask, tasks.size());
        storage.save(tasks.asList());
    }
}
