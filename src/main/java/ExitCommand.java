public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // Nothing to do here; XiaoZhi prints the farewell after the loop ends.
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
