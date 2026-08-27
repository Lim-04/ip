import java.util.ArrayList;

public class XiaoZhi {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showBanner();
        ui.showGreeting();
        store(ui);
        ui.showFarewell();
    }

    // The set of commands XiaoZhi understands
    private enum Command {
        LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN
    }

    private static void store(Ui ui) {
        Storage storage = new Storage("./data/xiaozhi.txt");
        ArrayList<Task> taskList = storage.load();
        String input = ui.readCommand();

        while (!input.equals("bye")) {
            String commandWord = input.split(" ")[0]; // To know what this input is supposed to do
            Command command;
            try {
                command = Command.valueOf(commandWord.toUpperCase());
            } catch (IllegalArgumentException e) {
                command = Command.UNKNOWN;
            }

            try {
                switch (command) {
                case LIST -> ui.showList(taskList);

                case MARK -> {
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
                    if (targetIndex < 0 || targetIndex >= taskList.size()) {
                        throw new XiaoZhiException(
                                "Task " + (targetIndex + 1) + " doesn't exist. You have " + taskList.size() + " task(s).");
                    }
                    taskList.get(targetIndex).markAsDone();
                    ui.showMarked(taskList.get(targetIndex));
                }

                case UNMARK -> {
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
                    if (targetIndex < 0 || targetIndex >= taskList.size()) {
                        throw new XiaoZhiException(
                                "Task " + (targetIndex + 1) + " doesn't exist. You have " + taskList.size() + " task(s).");
                    }
                    taskList.get(targetIndex).markAsNotDone();
                    ui.showUnmarked(taskList.get(targetIndex));
                }

                case DELETE -> {
                    String[] tokens = input.split(" ");
                    if (tokens.length < 2) {
                        throw new XiaoZhiException("Please specify which task number to delete.");
                    }
                    int targetIndex;
                    try {
                        targetIndex = Integer.parseInt(tokens[1]) - 1; // convert to 0-based index
                    } catch (NumberFormatException e) {
                        throw new XiaoZhiException("\"" + tokens[1] + "\" isn't a valid task number.");
                    }
                    if (targetIndex < 0 || targetIndex >= taskList.size()) {
                        throw new XiaoZhiException(
                                "Task " + (targetIndex + 1) + " doesn't exist. You have " + taskList.size() + " task(s).");
                    }
                    Task removedTask = taskList.remove(targetIndex);
                    ui.showRemoved(removedTask, taskList.size());
                }

                case TODO -> {
                    String description = input.length() > "todo ".length()
                            ? input.substring("todo ".length()) : ""; // description after the command word
                    if (description.isBlank()) {
                        throw new XiaoZhiException("The description of a todo cannot be empty.");
                    }
                    Todo newTask = new Todo(description);
                    taskList.add(newTask);
                    ui.showAdded(newTask, taskList.size());
                }

                case DEADLINE -> {
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
                    Deadline newTask = new Deadline(parts[0].trim(), Dates.parse(parts[1].trim()));
                    taskList.add(newTask);
                    ui.showAdded(newTask, taskList.size());
                }

                case EVENT -> {
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
                    Event newTask = new Event(fromParts[0].trim(),
                            Dates.parse(toParts[0].trim()), Dates.parse(toParts[1].trim()));
                    taskList.add(newTask);
                    ui.showAdded(newTask, taskList.size());
                }

                case UNKNOWN -> throw new XiaoZhiException("I don't recognise that command: " + commandWord);
                }
                storage.save(taskList);
            } catch (XiaoZhiException e) {
                ui.showError(e.getMessage());
            }

            input = ui.readCommand();
        }
    }
}
