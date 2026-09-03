package xiaozhi.ui;

import java.util.ArrayList;
import java.util.Scanner;

import xiaozhi.task.Task;
import xiaozhi.task.TaskList;

/**
 * Owns all of XiaoZhi's console input and output.
 * <p>
 * No other class reads from {@link System#in} or writes to
 * {@link System#out} directly; they go through this Ui instead, so the
 * exact wording and formatting of every message lives in one place.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Creates a Ui that reads user input from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads one line of input typed by the user.
     *
     * @return The line the user typed.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints XiaoZhi's ASCII-art banner.
     */
    public void showBanner() {
        String banner = """
            __  ___            ______     _
            \\ \\/ (_) __ _  ___|__  / |__ (_)
             \\  /| |/ _` |/ _ \\ / /| '_ \\| |
             /  \\| | (_| | (_) / /_| | | | |
            /_/\\_\\_|\\__,_|\\___/____|_| |_|_|
            """;
        System.out.println(banner);
    }

    /**
     * Prints the greeting shown right after the banner.
     */
    public void showGreeting() {
        System.out.println("Hi! I'm XiaoZhi.\nWhat's the task for today?");
    }

    /**
     * Prints the farewell shown once XiaoZhi's main loop ends.
     */
    public void showFarewell() {
        System.out.println("Bye, See you soon!");
    }

    /**
     * Reports that a task was added.
     *
     * @param task The task that was added.
     * @param taskCount The number of tasks now in the list, including this one.
     */
    public void showAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Reports that a task was removed.
     *
     * @param task The task that was removed.
     * @param taskCount The number of tasks left in the list.
     */
    public void showRemoved(Task task, int taskCount) {
        System.out.println("Got it, I've deleted this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Reports that a task was marked done.
     *
     * @param task The task that was marked done.
     */
    public void showMarked(Task task) {
        System.out.println("Roger! I've marked it as done: \n  " + task);
    }

    /**
     * Reports that a task was marked not done.
     *
     * @param task The task that was marked not done.
     */
    public void showUnmarked(Task task) {
        System.out.println("Okay, I've unmarked this: \n  " + task);
    }

    /**
     * Prints every task currently in the given task list, numbered from 1.
     *
     * @param tasks The task list to show.
     */
    public void showList(TaskList tasks) {
        System.out.println("Tasks for today:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Prints every task in {@code matches}, numbered from 1, under a heading
     * announcing they are the tasks that matched a find search.
     *
     * @param matches The matching tasks to show.
     */
    public void showFound(ArrayList<Task> matches) {
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + "." + matches.get(i));
        }
    }

    /**
     * Reports an error to the user.
     *
     * @param message Description of what went wrong, suitable for display to the user.
     */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }
}
