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

    private static void store() {
        int currIndex = 0;
        Task[] taskList = new Task[100];
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.equals("bye")) {
            if (input.equals("list")) {
                System.out.println("Tasks for today:");
                for (int i = 0; i < currIndex; i++) {
                    System.out.println((i + 1) + "." + taskList[i]);
                }
            } else if (input.split(" ")[0].equals("mark")) {
                int targetIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                taskList[targetIndex].markAsDone();
                System.out.println("Roger! I've marked it as done: \n  " + taskList[targetIndex]);
            } else if (input.split(" ")[0].equals("unmark")) {
                int targetIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                taskList[targetIndex].markAsNotDone();
                System.out.println("Okay, I've unmarked this: \n  " + taskList[targetIndex]);
            } else {
                taskList[currIndex] = new Task(input);
                System.out.println("Added: " + input);
                currIndex++;
            }
            input = scanner.nextLine();
        }
    }
}
