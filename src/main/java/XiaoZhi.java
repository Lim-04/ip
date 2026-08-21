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

            try {
                if (input.equals("list")) {
                    System.out.println("Tasks for today:");
                    for (int i = 0; i < currIndex; i++) {
                        System.out.println((i + 1) + "." + taskList[i]);
                    }

                } else if (command.equals("mark")) {
                    String[] tokens = input.split(" ");
                    if (tokens.length < 2) {
                        throw new XiaoZhiException("Please specify which task number to mark.");
                    }
                    int targetIndex;
                    try {
                        targetIndex = Integer.parseInt(tokens[1]) - 1; // convert to 0-based index
                    } catch (NumberFormatException e) {
                        throw new XiaoZhiException("\"" + tokens[1] + "\" isn't a valid task number.");
                    }
                    if (targetIndex < 0 || targetIndex >= currIndex) {
                        throw new XiaoZhiException(
                                "Task " + (targetIndex + 1) + " doesn't exist. You have " + currIndex + " task(s).");
                    }
                    taskList[targetIndex].markAsDone();
                    System.out.println("Roger! I've marked it as done: \n  " + taskList[targetIndex]);

                } else if (command.equals("unmark")) {
                    String[] tokens = input.split(" ");
                    if (tokens.length < 2) {
                        throw new XiaoZhiException("Please specify which task number to unmark.");
                    }
                    int targetIndex;
                    try {
                        targetIndex = Integer.parseInt(tokens[1]) - 1; // convert to 0-based index
                    } catch (NumberFormatException e) {
                        throw new XiaoZhiException("\"" + tokens[1] + "\" isn't a valid task number.");
                    }
                    if (targetIndex < 0 || targetIndex >= currIndex) {
                        throw new XiaoZhiException(
                                "Task " + (targetIndex + 1) + " doesn't exist. You have " + currIndex + " task(s).");
                    }
                    taskList[targetIndex].markAsNotDone();
                    System.out.println("Okay, I've unmarked this: \n  " + taskList[targetIndex]);

                } else if (command.equals("todo")) {
                    String description = input.length() > "todo ".length()
                            ? input.substring("todo ".length()) : ""; // description after the command word
                    if (description.isBlank()) {
                        throw new XiaoZhiException("The description of a todo cannot be empty.");
                    }
                    taskList[currIndex] = new Todo(description);
                    currIndex++;
                    printAdded(taskList[currIndex - 1], currIndex);

                } else if (command.equals("deadline")) {
                    String rest = input.length() > "deadline ".length()
                            ? input.substring("deadline ".length()) : ""; // removing the command from the input
                    if (rest.isBlank()) {
                        throw new XiaoZhiException("The description of a deadline cannot be empty.");
                    }
                    String padded = " " + rest; // so "/by" is caught even right after the command
                    if (!padded.contains(" /by ")) {
                        throw new XiaoZhiException(
                                "A deadline needs a /by date. Try: deadline <description> /by <date>");
                    }
                    String[] parts = padded.split(" /by ", 2); // index 0 is the description, 1 is the date
                    if (parts[0].isBlank()) {
                        throw new XiaoZhiException("The description of a deadline cannot be empty.");
                    }
                    if (parts[1].isBlank()) {
                        throw new XiaoZhiException("The /by date of a deadline cannot be empty.");
                    }
                    taskList[currIndex] = new Deadline(parts[0].trim(), parts[1].trim());
                    currIndex++;
                    printAdded(taskList[currIndex - 1], currIndex);

                } else if (command.equals("event")) {
                    String rest = input.length() > "event ".length()
                            ? input.substring("event ".length()) : "";
                    if (rest.isBlank()) {
                        throw new XiaoZhiException("The description of an event cannot be empty.");
                    }
                    String padded = " " + rest;
                    if (!padded.contains(" /from ")) {
                        throw new XiaoZhiException(
                                "An event needs a /from time. Try: event <description> /from <start> /to <end>");
                    }
                    String[] fromParts = padded.split(" /from ", 2);
                    if (fromParts[0].isBlank()) {
                        throw new XiaoZhiException("The description of an event cannot be empty.");
                    }
                    String afterFrom = " " + fromParts[1];
                    if (!afterFrom.contains(" /to ")) {
                        throw new XiaoZhiException(
                                "An event needs a /to time. Try: event <description> /from <start> /to <end>");
                    }
                    String[] toParts = afterFrom.split(" /to ", 2);
                    if (toParts[0].isBlank()) {
                        throw new XiaoZhiException("The /from time of an event cannot be empty.");
                    }
                    if (toParts[1].isBlank()) {
                        throw new XiaoZhiException("The /to time of an event cannot be empty.");
                    }
                    taskList[currIndex] = new Event(fromParts[0].trim(), toParts[0].trim(), toParts[1].trim());
                    currIndex++;
                    printAdded(taskList[currIndex - 1], currIndex);

                } else {
                    throw new XiaoZhiException("I don't recognise that command: " + command);
                }
            } catch (XiaoZhiException e) {
                System.out.println("OOPS!!! " + e.getMessage());
            }

            input = scanner.nextLine();
        }
    }
}
