/**
 * Represents a task that XiaoZhi keeps track of.
 * <p>
 * A task has a description and a completion status, and can be marked
 * done/not done, printed for display, or converted into the plain-text
 * format used to save it to disk.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Returns the single-letter code (e.g. "T", "D", "E") used to identify
     * this task's type when it is saved to disk.
     */
    public abstract String getTypeIcon();

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns this task encoded as one line of the save-file format:
     * {@code <type> | <isDone> | <description>}. Subclasses append any
     * extra fields they have (e.g. a deadline's date) after this.
     *
     * @return The task encoded for storage on disk.
     */
    public String toSaveFormat() {
        return getTypeIcon() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
