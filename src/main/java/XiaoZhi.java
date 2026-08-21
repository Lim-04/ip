import java.util.Scanner;

public class XiaoZhi {
    public static void main(String[] args) {
        printBanner();
        printGreet();
        store();
        printFarewell();
    }

    private static void printBanner() {
        String banner = """
            __  ___            ______     _ 
            \\ \\/ (_) __ _  ___|__  / |__ (_)
             \\  /| |/ _` |/ _ \\ / /| '_ \\| |
             /  \\| | (_| | (_) / /_| | | | |
            /_/\\_\\_|\\__,_|\\___/____|_| |_|_|
            """;
        System.out.println(banner);
    }

    private static void printGreet() {
        System.out.println("Hi! I'm XiaoZhi.\nWhat's the task for today?");
    }

    private static void printFarewell() {
        System.out.println("Bye, See you soon!");
    }

    private static void printAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void store() {
        int currIndex = 0;
        Task[] taskList = new Task[100];
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.equals("bye")) {
            String command = input.split(" ")[0]; // To know what this input is supposed to do

            if (input.equals("list")) {
                System.out.println("Tasks for today:");
                for (int i = 0; i < currIndex; i++) {
                    System.out.println((i + 1) + "." + taskList[i]);
                }
            } else if (command.equals("mark")) {
                int targetIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                taskList[targetIndex].markAsDone();
                System.out.println("Roger! I've marked it as done: \n  " + taskList[targetIndex]);

            } else if (command.equals("unmark")) {
                int targetIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                taskList[targetIndex].markAsNotDone();
                System.out.println("Okay, I've unmarked this: \n  " + taskList[targetIndex]);

            } else if (command.equals("todo")) {
                String description = input.substring("todo ".length());
                taskList[currIndex] = new Todo(description);
                currIndex++;
                printAdded(taskList[currIndex - 1], currIndex);

            } else if (command.equals("deadline")) {
                String rest = input.substring("deadline ".length());    // Removing the command from the input
                String[] parts = rest.split(" /by ", 2);
                taskList[currIndex] = new Deadline(parts[0], parts[1]); // index 0 is the description, 1 is the date
                currIndex++;
                printAdded(taskList[currIndex - 1], currIndex);

            } else if (command.equals("event")) {
                String rest = input.substring("event ".length());
                String[] fromParts = rest.split(" /from ", 2);
                String[] toParts = fromParts[1].split(" /to ", 2);
                taskList[currIndex] = new Event(fromParts[0], toParts[0], toParts[1]);
                currIndex++;
                printAdded(taskList[currIndex - 1], currIndex);

            } else {
                System.out.println("Sorry, I don't recognise that command: " + command);
            }
            input = scanner.nextLine();
        }
    }
}
