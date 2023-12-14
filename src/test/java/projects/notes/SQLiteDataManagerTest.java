package projects.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SQLiteDataManagerTest {
    private SQLiteDataManager sqLiteDataManager;
    private ObservableList<Note> testNotes;

    @BeforeEach
    void setUp() {
        sqLiteDataManager = new SQLiteDataManager();
        testNotes = createTestNotes();
    }

    @AfterEach
    void tearDown() {
        sqLiteDataManager.closeConnection();
    }

    @Test
    public void testSaveAndLoadNotes() {
        sqLiteDataManager.saveNotes(testNotes);
        ObservableList<Note> loadedNotes = sqLiteDataManager.loadNotes();
        assertEquals(testNotes, loadedNotes);
    }

    private ObservableList<Note> createTestNotes() {
        ObservableList<Note> notes = FXCollections.observableArrayList();
        notes.add(new Note("Test Note 1", "Test Content 1"));
        notes.add(new Note("Test Note 2", "Test Content 2"));
        notes.add(new Note("Test Note 3", "Test Content 3"));
        return notes;
    }

}