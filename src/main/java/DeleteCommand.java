public class DeleteCommand extends Command {
    private final int targetIndex;

    public DeleteCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

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
