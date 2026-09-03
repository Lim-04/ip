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
     * Prints each of the given lines to the console, one per line.
     * <p>
     * Every other method in this class that prints more than one line
     * (e.g. {@link #showAdded(Task, int)}) goes through this, instead of
     * making its own sequence of {@code System.out.println} calls, so
     * that "one line per varargs element" stays the one place that idea
     * is expressed. Called with a {@code String[]} where the number of
     * lines is only known at runtime, e.g. {@link #showList(TaskList)}.
     *
     * @param lines The lines to print, in order.
     */
    private void print(String... lines) {
        for (String line : lines) {
            System.out.println(line);
        }
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
        print(banner);
    }

    /**
     * Prints the greeting shown right after the banner.
     */
    public void showGreeting() {
        print("Hi! I'm XiaoZhi.", "What's the task for today?");
    }

    /**
     * Prints the farewell shown once XiaoZhi's main loop ends.
     */
    public void showFarewell() {
        print("Bye, See you soon!");
    }

    /**
     * Reports that a task was added.
     *
     * @param task The task that was added.
     * @param taskCount The number of tasks now in the list, including this one.
     */
    public void showAdded(Task task, int taskCount) {
        print("Got it. I've added this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Reports that a task was removed.
     *
     * @param task The task that was removed.
     * @param taskCount The number of tasks left in the list.
     */
    public void showRemoved(Task task, int taskCount) {
        print("Got it, I've deleted this task:",
                "  " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Reports that a task was marked done.
     *
     * @param task The task that was marked done.
     */
    public void showMarked(Task task) {
        print("Roger! I've marked it as done:", "  " + task);
    }

    /**
     * Reports that a task was marked not done.
     *
     * @param task The task that was marked not done.
     */
    public void showUnmarked(Task task) {
        print("Okay, I've unmarked this:", "  " + task);
    }

    /**
     * Prints every task currently in the given task list, numbered from 1.
     *
     * @param tasks The task list to show.
     */
    public void showList(TaskList tasks) {
        print(numberedLines("Tasks for today:", tasks.asList()));
    }

    /**
     * Prints every task in {@code matches}, numbered from 1, under a heading
     * announcing they are the tasks that matched a find search.
     *
     * @param matches The matching tasks to show.
     */
    public void showFound(ArrayList<Task> matches) {
        print(numberedLines("Here are the matching tasks in your list:", matches));
    }

    /**
     * Builds a {@code heading} followed by every task in {@code tasks}, numbered from 1.
     *
     * @param heading The line to show above the numbered tasks.
     * @param tasks The tasks to number.
     * @return {@code heading} and the numbered tasks, ready to hand to {@link #print(String...)}.
     */
    private String[] numberedLines(String heading, ArrayList<Task> tasks) {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = heading;
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
        }
        return lines;
    }

    /**
     * Reports an error to the user.
     *
     * @param message Description of what went wrong, suitable for display to the user.
     */
    public void showError(String message) {
        print("OOPS!!! " + message);
    }
}
