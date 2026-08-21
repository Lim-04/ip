public class Task {
    protected String taskDescription;
    protected boolean isDone;

    public Task(String description) {
        this.taskDescription = description;
        this.isDone = false;
    }

    public String getStatus() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatus() + "] " + taskDescription;
    }
}
