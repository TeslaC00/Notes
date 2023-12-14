package projects.notes;

import javafx.collections.ObservableList;

public interface DataManager {
    void saveNotes(ObservableList<Note> notes);

    ObservableList<Note> loadNotes();
}
