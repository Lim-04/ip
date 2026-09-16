package xiaozhi.parser;

import xiaozhi.command.AddCommand;
import xiaozhi.command.Command;
import xiaozhi.command.DeleteCommand;
import xiaozhi.command.ExitCommand;
import xiaozhi.command.FindCommand;
import xiaozhi.command.ListCommand;
import xiaozhi.command.MarkCommand;
import xiaozhi.command.UndoCommand;
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
        LIST, MARK, UNMARK, DELETE, FIND, TODO, DEADLINE, EVENT, UNDO, BYE, UNKNOWN
    }

    private Parser() {
    }

    /**
     * Parses one full line of user input into the {@link Command} it represents.
     * <p>
     * Dispatches to a per-command-word helper that knows that command's own
     * syntax; this method itself only identifies which command word was used.
     *
     * @param fullCommand The raw line of input the user typed.
     * @return The command the input represents.
     * @throws XiaoZhiException If the input is not a recognised command, or is
     *         missing a part a recognised command requires (e.g. a task number,
     *         or a deadline's {@code /by} date).
     */
    public static Command parse(String fullCommand) throws XiaoZhiException {
        assert fullCommand != null
                : "Input line should never be null; it comes from Scanner.nextLine() or a GUI text "
                + "field, neither of which returns null here.";
        String commandWord = fullCommand.split(" ")[0]; // To know what this input is supposed to do
        CommandWord commandType;
        try {
            commandType = CommandWord.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            commandType = CommandWord.UNKNOWN;
        }

        return switch (commandType) {
            case LIST -> new ListCommand();
            case MARK -> new MarkCommand(parseTaskIndex(fullCommand, "mark"));
            case UNMARK -> new UnmarkCommand(parseTaskIndex(fullCommand, "unmark"));
            case DELETE -> new DeleteCommand(parseTaskIndex(fullCommand, "delete"));
            case FIND -> new FindCommand(parseFindKeyword(fullCommand));
            case TODO -> new AddCommand(parseTodo(fullCommand));
            case DEADLINE -> new AddCommand(parseDeadline(fullCommand));
            case EVENT -> new AddCommand(parseEvent(fullCommand));
            case UNDO -> new UndoCommand();
            case BYE -> new ExitCommand();
            case UNKNOWN -> throw new XiaoZhiException("I have not learned the word \"" + commandWord + "\".");
        };
    }

    /**
     * Parses the task number out of a {@code mark}/{@code unmark}/{@code delete}
     * command, shared since all three take nothing but a task number.
     *
     * @param fullCommand The raw line of input the user typed.
     * @param action Name of the action, used to phrase the missing-number error, e.g. {@code "mark"}.
     * @return The 0-based index of the target task.
     * @throws XiaoZhiException If no task number was given, or it isn't a number.
     */
    private static int parseTaskIndex(String fullCommand, String action) throws XiaoZhiException {
        String[] tokens = fullCommand.split(" ");
        if (tokens.length < 2) {
            throw new XiaoZhiException("Please specify which task number to " + action + ".");
        }
        try {
            return Integer.parseInt(tokens[1]) - 1; // convert to 0-based index
        } catch (NumberFormatException e) {
            throw new XiaoZhiException("\"" + tokens[1] + "\" isn't a valid task number.");
        }
    }

    /**
     * Parses the keyword out of a {@code find} command.
     *
     * @param fullCommand The raw line of input the user typed.
     * @return The keyword to search task descriptions for.
     * @throws XiaoZhiException If no keyword was given.
     */
    private static String parseFindKeyword(String fullCommand) throws XiaoZhiException {
        String keyword = fullCommand.length() > "find ".length()
                ? fullCommand.substring("find ".length())
                : "";
        if (keyword.isBlank()) {
            throw new XiaoZhiException("Please specify a keyword to search for.");
        }
        return keyword;
    }

    /**
     * Parses a {@code todo} command into the {@link Todo} it describes.
     *
     * @param fullCommand The raw line of input the user typed.
     * @return The todo the input describes.
     * @throws XiaoZhiException If the todo has no description.
     */
    private static Todo parseTodo(String fullCommand) throws XiaoZhiException {
        String description = fullCommand.length() > "todo ".length()
                ? fullCommand.substring("todo ".length())
                : "";
        if (description.isBlank()) {
            throw new XiaoZhiException("The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    /**
     * Parses a {@code deadline} command into the {@link Deadline} it describes.
     *
     * @param fullCommand The raw line of input the user typed.
     * @return The deadline the input describes.
     * @throws XiaoZhiException If the deadline is missing its description or its {@code /by} date.
     */
    private static Deadline parseDeadline(String fullCommand) throws XiaoZhiException {
        String rest = fullCommand.length() > "deadline ".length()
                ? fullCommand.substring("deadline ".length())
                : "";
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
        return new Deadline(parts[0].trim(), Dates.parse(parts[1].trim()));
    }

    /**
     * Parses an {@code event} command into the {@link Event} it describes.
     *
     * @param fullCommand The raw line of input the user typed.
     * @return The event the input describes.
     * @throws XiaoZhiException If the event is missing its description, its {@code /from} time,
     *         or its {@code /to} time.
     */
    private static Event parseEvent(String fullCommand) throws XiaoZhiException {
        String rest = fullCommand.length() > "event ".length()
                ? fullCommand.substring("event ".length())
                : "";
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
        return new Event(fromParts[0].trim(), Dates.parse(toParts[0].trim()), Dates.parse(toParts[1].trim()));
    }
}
