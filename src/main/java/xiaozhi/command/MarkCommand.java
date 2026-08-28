package xiaozhi.command;

import xiaozhi.exception.XiaoZhiException;
import xiaozhi.storage.Storage;
import xiaozhi.task.TaskList;
import xiaozhi.ui.Ui;

/**
 * Marks a task in the task list as done.
 */
public class MarkCommand extends Command {
    private final int targetIndex;

    /**
     * Creates a MarkCommand for the task at the given position.
     *
     * @param targetIndex 0-based index, within the task list, of the task to mark.
     */
    public MarkCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Marks the target task as done, reports it through {@code ui}, and
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
        tasks.get(targetIndex).markAsDone();
        ui.showMarked(tasks.get(targetIndex));
        storage.save(tasks.asList());
    }
}
