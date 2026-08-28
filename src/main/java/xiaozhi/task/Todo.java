package xiaozhi.task;

/**
 * A task with no date attached, other than a description.
 */
public class Todo extends Task {

    /**
     * Creates a todo with the given description.
     *
     * @param description What the todo is.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
