package xiaozhi.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import xiaozhi.command.AddCommand;
import xiaozhi.command.Command;
import xiaozhi.command.CommandHistory;
import xiaozhi.command.DeleteCommand;
import xiaozhi.command.ExitCommand;
import xiaozhi.command.FindCommand;
import xiaozhi.command.ListCommand;
import xiaozhi.command.MarkCommand;
import xiaozhi.command.UndoCommand;
import xiaozhi.command.UnmarkCommand;
import xiaozhi.exception.XiaoZhiException;
import xiaozhi.storage.Storage;
import xiaozhi.task.Task;
import xiaozhi.task.TaskList;
import xiaozhi.task.Todo;
import xiaozhi.testutil.OutputCapture;
import xiaozhi.ui.Ui;

/**
 * Tests {@link Parser}, covering every command word it recognises and every
 * validation error it can raise while doing so.
 * <p>
 * Several command types keep their target (a task index, a keyword, a
 * built {@link Task}) in a private field with no getter, so where a plain
 * {@code instanceof} check is not enough, the returned {@link Command} is
 * actually executed against a small {@link TaskList} to observe what it
 * would do.
 */
public class ParserTest {

    @TempDir
    Path tempDir;

    private Storage newStorage() {
        return new Storage(tempDir.resolve("xiaozhi.txt").toString());
    }

    // ---------- list / undo / bye / unknown ----------

    @Test
    public void parse_list_returnsListCommand() throws XiaoZhiException {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
    }

    @Test
    public void parse_undo_returnsUndoCommand() throws XiaoZhiException {
        assertInstanceOf(UndoCommand.class, Parser.parse("undo"));
    }

    @Test
    public void parse_bye_returnsExitCommand() throws XiaoZhiException {
        Command command = Parser.parse("bye");

        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    public void parse_commandWordIsCaseInsensitive() throws XiaoZhiException {
        assertInstanceOf(ListCommand.class, Parser.parse("LIST"));
        assertInstanceOf(ExitCommand.class, Parser.parse("Bye"));
    }

    @Test
    public void parse_unrecognisedWord_throwsWithWordQuoted() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class, () -> Parser.parse("foobar"));

        assertEquals("I have not learned the word \"foobar\".", thrown.getMessage());
    }

    // ---------- todo ----------

    @Test
    public void parse_todoWithDescription_addsATodoWithThatDescription() throws Exception {
        AddCommand command = (AddCommand) Parser.parse("todo read book");
        TaskList tasks = new TaskList();

        command.execute(tasks, new Ui(), newStorage(), new CommandHistory());

        assertInstanceOf(Todo.class, tasks.get(0));
        assertEquals("read book", tasks.get(0).getDescription());
    }

    @Test
    public void parse_todoWithNoDescription_throws() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class, () -> Parser.parse("todo"));

        assertEquals("The description of a todo cannot be empty.", thrown.getMessage());
    }

    @Test
    public void parse_todoWithOnlyBlankDescription_throws() {
        assertThrows(XiaoZhiException.class, () -> Parser.parse("todo    "));
    }

    // ---------- deadline ----------

    @Test
    public void parse_deadlineWithDescriptionAndByDate_addsADeadline() throws Exception {
        AddCommand command = (AddCommand) Parser.parse("deadline return book /by 2019-12-02");
        TaskList tasks = new TaskList();

        command.execute(tasks, new Ui(), newStorage(), new CommandHistory());

        assertEquals("[D][ ] return book (by: Dec 02 2019)", tasks.get(0).toString());
    }

    @Test
    public void parse_deadlineWithNoContentAtAll_throwsEmptyDescription() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class, () -> Parser.parse("deadline"));

        assertEquals("The description of a deadline cannot be empty.", thrown.getMessage());
    }

    @Test
    public void parse_deadlineWithOnlyByClause_throwsEmptyDescription() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class,
                () -> Parser.parse("deadline /by 2019-12-02"));

        assertEquals("The description of a deadline cannot be empty.", thrown.getMessage());
    }

    @Test
    public void parse_deadlineWithNoByMarker_throwsWithUsageHint() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class,
                () -> Parser.parse("deadline return book"));

        assertEquals("A deadline needs a /by date. Try: deadline <description> /by <date>", thrown.getMessage());
    }

    @Test
    public void parse_deadlineWithBlankByDate_throws() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class,
                () -> Parser.parse("deadline return book /by "));

        assertEquals("The /by date of a deadline cannot be empty.", thrown.getMessage());
    }

    @Test
    public void parse_deadlineWithInvalidDate_throws() {
        assertThrows(XiaoZhiException.class, () -> Parser.parse("deadline return book /by tomorrow"));
    }

    // ---------- event ----------

    @Test
    public void parse_eventWithDescriptionAndDates_addsAnEvent() throws Exception {
        AddCommand command = (AddCommand) Parser.parse(
                "event project meeting /from 2019-08-06 /to 2019-08-07");
        TaskList tasks = new TaskList();

        command.execute(tasks, new Ui(), newStorage(), new CommandHistory());

        assertEquals("[E][ ] project meeting (from: Aug 06 2019 to: Aug 07 2019)", tasks.get(0).toString());
    }

    @Test
    public void parse_eventWithNoContentAtAll_throwsEmptyDescription() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class, () -> Parser.parse("event"));

        assertEquals("The description of an event cannot be empty.", thrown.getMessage());
    }

    @Test
    public void parse_eventWithNoFromMarker_throwsWithUsageHint() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class,
                () -> Parser.parse("event project meeting"));

        assertEquals("An event needs a /from time. Try: event <description> /from <start> /to <end>",
                thrown.getMessage());
    }

    @Test
    public void parse_eventWithNoToMarker_throwsWithUsageHint() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class,
                () -> Parser.parse("event project meeting /from 2019-08-06"));

        assertEquals("An event needs a /to time. Try: event <description> /from <start> /to <end>",
                thrown.getMessage());
    }

    @Test
    public void parse_eventWithInvalidFromDate_throws() {
        assertThrows(XiaoZhiException.class,
                () -> Parser.parse("event project meeting /from tomorrow /to 2019-08-07"));
    }

    // ---------- mark / unmark / delete ----------

    @Test
    public void parse_markWithNumber_marksThatTask() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        Command command = Parser.parse("mark 1");

        assertInstanceOf(MarkCommand.class, command);
        command.execute(tasks, new Ui(), newStorage(), new CommandHistory());
        assertTrue(tasks.get(0).isDone());
    }

    @Test
    public void parse_unmarkWithNumber_returnsUnmarkCommand() throws XiaoZhiException {
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
    }

    @Test
    public void parse_deleteWithNumber_returnsDeleteCommand() throws XiaoZhiException {
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
    }

    @Test
    public void parse_markWithNoNumber_throws() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class, () -> Parser.parse("mark"));

        assertEquals("Please specify which task number to mark.", thrown.getMessage());
    }

    @Test
    public void parse_deleteWithNoNumber_throws() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class, () -> Parser.parse("delete"));

        assertEquals("Please specify which task number to delete.", thrown.getMessage());
    }

    @Test
    public void parse_markWithNonNumericArgument_throwsWithArgumentQuoted() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class, () -> Parser.parse("mark abc"));

        assertEquals("\"abc\" isn't a valid task number.", thrown.getMessage());
    }

    // ---------- find ----------

    @Test
    public void parse_findWithKeyword_returnsFindCommandThatFiltersByIt() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("write essay"));
        Command command = Parser.parse("find book");

        assertInstanceOf(FindCommand.class, command);
        String output = OutputCapture.capture(
                () -> command.execute(tasks, new Ui(), newStorage(), new CommandHistory()));
        assertEquals(
                "These are the tasks that echo your search:" + System.lineSeparator()
                        + "1.[T][ ] read book" + System.lineSeparator(),
                output);
    }

    @Test
    public void parse_findWithNoKeyword_throws() {
        XiaoZhiException thrown = assertThrows(XiaoZhiException.class, () -> Parser.parse("find"));

        assertEquals("Please specify a keyword to search for.", thrown.getMessage());
    }

    @Test
    public void parse_findWithOnlyBlankKeyword_throws() {
        assertThrows(XiaoZhiException.class, () -> Parser.parse("find   "));
    }
}
