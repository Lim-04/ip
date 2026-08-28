public class MarkCommand extends Command {
    private final int targetIndex;

    public MarkCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

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
