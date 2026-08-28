package xiaozhi.parser;

import xiaozhi.command.AddCommand;
import xiaozhi.command.Command;
import xiaozhi.command.DeleteCommand;
import xiaozhi.command.ExitCommand;
import xiaozhi.command.ListCommand;
import xiaozhi.command.MarkCommand;
import xiaozhi.command.UnmarkCommand;
import xiaozhi.exception.XiaoZhiException;
import xiaozhi.task.Deadline;
import xiaozhi.task.Event;
import xiaozhi.task.Todo;
import xiaozhi.util.Dates;

/**
 * Turns one line of raw user input into a {@link Command}.
 * <p>
 * Only checks that a command is syntactically well-formed (e.g. that a
 * deadline has a {@code /by} date, or that a task number is a number at
 * all); whether that task number actually exists is left to the resulting
 * command's own {@code execute()}, since only it has access to the task list.
 */
public class Parser {
    // The set of command words XiaoZhi understands
    private enum CommandWord {
        LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, BYE, UNKNOWN
    }

    private Parser() {
    }

    /**
     * Parses one full line of user input into the {@link Command} it represents.
     *
     * @param fullCommand The raw line of input the user typed.
     * @return The command the input represents.
     * @throws XiaoZhiException If the input is not a recognised command, or is
     *         missing a part a recognised command requires (e.g. a task number,
     *         or a deadline's {@code /by} date).
     */
    public static Command parse(String fullCommand) throws XiaoZhiException {
        String commandWord = fullCommand.split(" ")[0]; // To know what this input is supposed to do
        CommandWord commandType;
        try {
            commandType = CommandWord.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            commandType = CommandWord.UNKNOWN;
        }

        return switch (commandType) {
        case LIST -> new ListCommand();

        case MARK -> {
            String[] tokens = fullCommand.split(" ");
            if (tokens.length < 2) {
                throw new XiaoZhiException("Please specify which task number to mark.");
            }
            int targetIndex;
            try {
                targetIndex = Integer.parseInt(tokens[1]) - 1; // convert to 0-based index
            } catch (NumberFormatException e) {
                throw new XiaoZhiException("\"" + tokens[1] + "\" isn't a valid task number.");
            }
            yield new MarkCommand(targetIndex);
        }

        case UNMARK -> {
            String[] tokens = fullCommand.split(" ");
            if (tokens.length < 2) {
                throw new XiaoZhiException("Please specify which task number to unmark.");
            }
            int targetIndex;
            try {
                targetIndex = Integer.parseInt(tokens[1]) - 1;
            } catch (NumberFormatException e) {
                throw new XiaoZhiException("\"" + tokens[1] + "\" isn't a valid task number.");
            }
            yield new UnmarkCommand(targetIndex);
        }

        case DELETE -> {
            String[] tokens = fullCommand.split(" ");
            if (tokens.length < 2) {
                throw new XiaoZhiException("Please specify which task number to delete.");
            }
            int targetIndex;
            try {
                targetIndex = Integer.parseInt(tokens[1]) - 1;
            } catch (NumberFormatException e) {
                throw new XiaoZhiException("\"" + tokens[1] + "\" isn't a valid task number.");
            }
            yield new DeleteCommand(targetIndex);
        }

        case TODO -> {
            String description = fullCommand.length() > "todo ".length()
                    ? fullCommand.substring("todo ".length()) : "";
            if (description.isBlank()) {
                throw new XiaoZhiException("The description of a todo cannot be empty.");
            }
            yield new AddCommand(new Todo(description));
        }

        case DEADLINE -> {
            String rest = fullCommand.length() > "deadline ".length()
                    ? fullCommand.substring("deadline ".length()) : "";
            if (rest.isBlank()) {
                throw new XiaoZhiException("The description of a deadline cannot be empty.");
            }
            String padded = " " + rest;
            if (!padded.contains(" /by ")) {
                throw new XiaoZhiException(
                        "A deadline needs a /by date. Try: deadline <description> /by <date>");
            }
            String[] parts = padded.split(" /by ", 2);
            if (parts[0].isBlank()) {
                throw new XiaoZhiException("The description of a deadline cannot be empty.");
            }
            if (parts[1].isBlank()) {
                throw new XiaoZhiException("The /by date of a deadline cannot be empty.");
            }
            yield new AddCommand(new Deadline(parts[0].trim(), Dates.parse(parts[1].trim())));
        }

        case EVENT -> {
            String rest = fullCommand.length() > "event ".length()
                    ? fullCommand.substring("event ".length()) : "";
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
            yield new AddCommand(new Event(fromParts[0].trim(),
                    Dates.parse(toParts[0].trim()), Dates.parse(toParts[1].trim())));
        }

        case BYE -> new ExitCommand();

        case UNKNOWN -> throw new XiaoZhiException("I don't recognise that command: " + commandWord);
        };
    }
}
