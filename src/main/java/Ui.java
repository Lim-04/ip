import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

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

    public void showGreeting() {
        System.out.println("Hi! I'm XiaoZhi.\nWhat's the task for today?");
    }

    public void showFarewell() {
        System.out.println("Bye, See you soon!");
    }

    public void showAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    public void showRemoved(Task task, int taskCount) {
        System.out.println("Got it, I've deleted this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    public void showMarked(Task task) {
        System.out.println("Roger! I've marked it as done: \n  " + task);
    }

    public void showUnmarked(Task task) {
        System.out.println("Okay, I've unmarked this: \n  " + task);
    }

    public void showList(ArrayList<Task> tasks) {
        System.out.println("Tasks for today:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }
}
