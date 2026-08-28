package xiaozhi.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import xiaozhi.task.Deadline;
import xiaozhi.task.Event;
import xiaozhi.task.Task;
import xiaozhi.task.Todo;

public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    public void saveThenLoad_multipleTaskTypes_roundTripsEveryField() {
        Storage storage = new Storage(tempDir.resolve("xiaozhi.txt").toString());

        Todo todo = new Todo("read book");
        Deadline deadline = new Deadline("return book", LocalDate.of(2019, 6, 6));
        Event event = new Event("project meeting", LocalDate.of(2019, 8, 6), LocalDate.of(2019, 8, 6));
        todo.markAsDone();

        ArrayList<Task> original = new ArrayList<>();
        original.add(todo);
        original.add(deadline);
        original.add(event);

        storage.save(original);
        ArrayList<Task> loaded = storage.load();

        assertEquals(original.size(), loaded.size());
        for (int i = 0; i < original.size(); i++) {
            assertEquals(original.get(i).toSaveFormat(), loaded.get(i).toSaveFormat());
        }
    }

    @Test
    public void load_fileDoesNotExist_returnsEmptyList() {
        Storage storage = new Storage(tempDir.resolve("does-not-exist.txt").toString());

        ArrayList<Task> loaded = storage.load();

        assertTrue(loaded.isEmpty());
    }

    @Test
    public void load_oneCorruptedLineAmongValidOnes_skipsOnlyTheCorruptedLine() throws IOException {
        Path saveFile = tempDir.resolve("xiaozhi.txt");
        Files.writeString(saveFile, String.join(System.lineSeparator(),
                "T | 0 | read book",
                "this line is not in the save format",
                "T | 1 | return book",
                ""));
        Storage storage = new Storage(saveFile.toString());

        ArrayList<Task> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals("T | 0 | read book", loaded.get(0).toSaveFormat());
        assertEquals("T | 1 | return book", loaded.get(1).toSaveFormat());
    }

    @Test
    public void save_parentFolderDoesNotExistYet_createsItAutomatically() {
        Path nestedFile = tempDir.resolve("data").resolve("xiaozhi.txt");
        Storage storage = new Storage(nestedFile.toString());

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        storage.save(tasks);

        assertTrue(Files.exists(nestedFile));
        assertEquals(1, storage.load().size());
    }
}
