package projects.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {

    private static final String NOTES_PATH = "src/main/resources/save.txt";
    public TextArea mainTextArea;
    public Button closeButton;
    public TextField titleTextField;
    public Button deleteNoteButton;
    public Button addNoteButton;
    public ComboBox<Note> notesComboBox;
    private File saveFile;
    private ArrayList<Note> notes;
    private boolean saveFileAlreadyExists;
    private int currentNoteIndex = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveFile();
        loadNotes();
        initNotesComboBox();
        closeButton.setOnAction(event -> {
            updateCurrentNote();
            saveNotes();
            App.closeApp();
        });
        deleteNoteButton.setOnAction(event -> deleteCurrentNote());
        addNoteButton.setOnAction(event -> addNewNote());
    }

    private void deleteCurrentNote() {
        Note removedNote = notes.remove(currentNoteIndex);
        notesComboBox.getItems().remove(removedNote);
        currentNoteIndex = notes.size() - 1;
        showNote(currentNoteIndex);
    }

    private void addNewNote() {
        Note note = new Note("", "");
        if (notes.isEmpty()) {
            notes.add(note);
            currentNoteIndex = 0;
            notesComboBox.getItems().add(note);
            notesComboBox.getSelectionModel().select(note);
        } else {
            updateCurrentNote();
            notes.add(note);
            currentNoteIndex = notes.indexOf(note);
            notesComboBox.getItems().add(note);
            notesComboBox.getSelectionModel().select(note);
        }
        showNote(currentNoteIndex);
    }

    private void initNotesComboBox() {
        ObservableList<Note> observableNotes = FXCollections.observableArrayList(notes);
        notesComboBox.getItems().addAll(observableNotes);
        notesComboBox.setOnAction(event -> {
            currentNoteIndex = notesComboBox.getSelectionModel().getSelectedIndex();
            showNote(currentNoteIndex);
        });
    }

    private void updateCurrentNote() {
        if (currentNoteIndex >= 0) {
            Note note = new Note(titleTextField.getText(), mainTextArea.getText());
            notes.set(currentNoteIndex, note);
        }
    }

    private void showNote(int index) {
        if (index >= 0) {
            Note note = notes.get(index);
            notesComboBox.getSelectionModel().clearAndSelect(currentNoteIndex);
            titleTextField.setText(note.title());
            mainTextArea.setText(note.description());
        } else {
            notesComboBox.getSelectionModel().clearSelection();
            addNewNote();
        }
    }

    private void initSaveFile() {
        saveFile = new File(NOTES_PATH);
        try {
            if (saveFile.createNewFile()) {
                System.out.println("New File created");
                saveFileAlreadyExists = false;
            } else {
                System.out.println("File already exists");
                saveFileAlreadyExists = true;
            }
        } catch (IOException io) {
            System.err.println("File path error");
        }
    }

    private void loadNotes() {
        if (saveFileAlreadyExists) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(saveFile))) {
                //noinspection unchecked
                notes = (ArrayList<Note>) inputStream.readObject();
                System.out.println("Loaded file successfully");
            } catch (IOException io) {
                System.err.println("Failed in locating file while loading\n" + io.getMessage());
            } catch (ClassNotFoundException ce) {
                System.err.println("Error in reading the class from file\n" + ce.getMessage());
            }
            currentNoteIndex = notes.size() - 1;
            showNote(currentNoteIndex);
        } else {
            notes = new ArrayList<>();
            addNewNote();
        }
    }

    private void saveNotes() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            outputStream.writeObject(notes);
            System.out.println("Saved file successfully");
        } catch (IOException io) {
            System.out.println("Failed in locating file while saving\n" + io.getMessage());
        }

    }
}
