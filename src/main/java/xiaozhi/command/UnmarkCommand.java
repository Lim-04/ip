package xiaozhi.command;

import xiaozhi.exception.XiaoZhiException;
import xiaozhi.storage.Storage;
import xiaozhi.task.TaskList;
import xiaozhi.ui.Ui;

/**
 * Marks a task in the task list as not done.
 */
public class UnmarkCommand extends Command {
    private final int targetIndex;

    /**
     * Creates an UnmarkCommand for the task at the given position.
     *
     * @param targetIndex 0-based index, within the task list, of the task to unmark.
     */
    public UnmarkCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Marks the target task as not done, reports it through {@code ui}, and
     * saves the updated list through {@code storage}.
     *
     * @throws XiaoZhiException If {@code targetIndex} is not a valid position in {@code tasks}.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws XiaoZhiException {
        if (targetIndex < 0 || targetIndex >= tasks.size()) {
            throw new XiaoZhiException(
                    "Task " + (targetIndex + 1) + " doesn't exist. You have " + tasks.size() + " task(s).");
        }
        tasks.get(targetIndex).markAsNotDone();
        ui.showUnmarked(tasks.get(targetIndex));
        storage.save(tasks.asList());
    }
}
