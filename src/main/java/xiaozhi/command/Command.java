package xiaozhi.command;

import xiaozhi.exception.XiaoZhiException;
import xiaozhi.storage.Storage;
import xiaozhi.task.TaskList;
import xiaozhi.ui.Ui;

/**
 * Represents an action XiaoZhi can carry out in response to a user command.
 * <p>
 * A Command is built by {@link xiaozhi.parser.Parser} from raw input text,
 * then run against the current task list, UI and storage.
 */
public abstract class Command {

    /**
     * Carries out this command's effect on the given task list, reporting
     * to the user through {@code ui} and persisting any change through
     * {@code storage}.
     *
     * @param tasks The current task list.
     * @param ui The UI to report results or errors through.
     * @param storage The storage to persist any change to the task list to.
     * @throws XiaoZhiException If the command cannot be carried out, e.g. an invalid task number.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws XiaoZhiException;

    /**
     * Returns whether this command should end XiaoZhi's main loop.
     * Defaults to {@code false}; only {@link ExitCommand} overrides this.
     *
     * @return {@code true} if XiaoZhi should exit after this command, {@code false} otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
