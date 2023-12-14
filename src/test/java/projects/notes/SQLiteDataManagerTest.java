package projects.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SQLiteDataManagerTest {

    private static SQLiteDataManager dataManager;

    @BeforeAll
    public static void setUp() {
        // Connect to the test database and create the table
        dataManager = new SQLiteDataManager();
    }

    @AfterAll
    public static void tearDown() {
        // Disconnect from the test database
        dataManager.closeConnection();
    }

    @Test
    public void testSaveAndLoadNotes() {
        ObservableList<Note> originalNotes = FXCollections.observableArrayList(
                new Note("Title1", "Content1"),
                new Note("Title2", "Content2")
        );

        // Save the original notes
        dataManager.saveNotes(originalNotes);

        // Load the notes from the database
        ObservableList<Note> loadedNotes = dataManager.loadNotes();

        // Assert that the loaded notes match the original notes
        assertEquals(originalNotes, loadedNotes, "Saved and loaded notes should be equal");
    }

    @Test
    public void testSaveAndLoadEmptyNotes() {
        ObservableList<Note> originalNotes = FXCollections.observableArrayList();

        // Save the original empty notes
        dataManager.saveNotes(originalNotes);

        // Load the notes from the database
        ObservableList<Note> loadedNotes = dataManager.loadNotes();

        // Assert that the loaded notes are empty
        assertTrue(loadedNotes.isEmpty(), "Saved and loaded empty notes should be equal");
    }
}
