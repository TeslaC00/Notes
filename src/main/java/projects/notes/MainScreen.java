package projects.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {

    //    Currently works but the combobox is not rendering correctly sometimes breaks the program
    private static final String SAVE_PATH = "src/main/resources/save.txt";
    public TextArea mainTextArea;
    public Button closeButton;
    public TextField titleTextField;
    public Button deleteNoteButton;
    public Button addNoteButton;
    public ChoiceBox<Note> notesChoiceBox;
    private File saveFile;
    private ObservableList<Note> notes;
    private boolean saveFileAlreadyExists;
    private int currentNoteIndex;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSaveFile();
        loadNotes();
        initNotesChoiceBox();
        closeButton.setOnAction(event -> {
            updateCurrentNote();
            saveNotes();
            App.closeApp();
        });
        deleteNoteButton.setOnAction(event -> deleteCurrentNote());
        addNoteButton.setOnAction(event -> addNewNote());
    }

    private void initNotesChoiceBox() {
        notesChoiceBox.setItems(notes);
        notesChoiceBox.setOnAction(event -> {
            updateCurrentNote();
            currentNoteIndex = notesChoiceBox.getSelectionModel().getSelectedIndex();
            showNote(currentNoteIndex);
        });
    }

    private void deleteCurrentNote() {
        notes.remove(currentNoteIndex);
        currentNoteIndex = notes.size() - 1;
        showNote(currentNoteIndex);
    }

    private void addNewNote() {
        Note note = new Note("", "");
        if (notes.isEmpty()) {
            notes.add(note);
            currentNoteIndex = 0;
        } else {
            updateCurrentNote();
            notes.add(note);
            currentNoteIndex = notes.indexOf(note);
        }
        showNote(currentNoteIndex);
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
            titleTextField.setText(note.title());
            mainTextArea.setText(note.description());
        } else {
            addNewNote();
        }
    }

    private void initSaveFile() {
        saveFile = new File(SAVE_PATH);
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
                ArrayList<Note> arrayList = (ArrayList<Note>) inputStream.readObject();
                notes = FXCollections.observableArrayList(arrayList);
                System.out.println("Loaded file successfully");
            } catch (IOException io) {
                System.err.println("Failed in file loading\n" + io.getMessage());
            } catch (ClassNotFoundException ce) {
                System.err.println("Error in reading the class from file\n" + ce.getMessage());
            }
            currentNoteIndex = notes.size() - 1;
            showNote(currentNoteIndex);
        } else {
            notes = FXCollections.observableArrayList();
            addNewNote();
        }
    }

    private void saveNotes() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            outputStream.writeObject(new ArrayList<>(notes));
            System.out.println("Saved file successfully");
        } catch (IOException io) {
            System.out.println("Failed in file saving\n" + io.getMessage());
        }

    }
}
