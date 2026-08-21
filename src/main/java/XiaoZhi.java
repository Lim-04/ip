import java.util.Scanner;

public class XiaoZhi {
    public static void main(String[] args) {
      printBanner();
      printGreet();
      echo();
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

    private static void echo() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (!input.equals("bye")) {
            System.out.println(input);
            input = scanner.nextLine();
        }
    }
}
