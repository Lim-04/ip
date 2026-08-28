package xiaozhi.task;

import java.util.ArrayList;

/**
 * Holds XiaoZhi's current list of tasks.
 * <p>
 * A thin wrapper around an {@link ArrayList} of {@link Task}, kept so that
 * {@link xiaozhi.command.Command} and {@link xiaozhi.ui.Ui} depend on a
 * task list rather than a raw collection type.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list wrapping an existing list of tasks, e.g. one just loaded from disk.
     *
     * @param tasks The tasks to start with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given position.
     *
     * @param index 0-based position of the task to remove.
     * @return The task that was removed.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given position, without removing it.
     *
     * @param index 0-based position of the task.
     * @return The task at that position.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns how many tasks are currently in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks, e.g. for {@link xiaozhi.storage.Storage} to save.
     *
     * @return The tasks, as a plain list.
     */
    public ArrayList<Task> asList() {
        return tasks;
    }

    /**
     * Finds every task whose description contains the given keyword.
     * The match is case-insensitive.
     *
     * @param keyword The keyword to search task descriptions for.
     * @return The matching tasks, in their original order. Empty if none match.
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        String needle = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(needle)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
