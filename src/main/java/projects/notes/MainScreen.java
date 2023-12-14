package projects.notes;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {

    public TextArea mainTextArea;
    public Button closeButton;
    public TextField titleTextField;
    public Button deleteNoteButton;
    public Button addNoteButton;
    public ComboBox<Note> notesComboBox;
    private DataManager dataManager;
    private ObservableList<Note> notes;
    private AutoSaver autoSaver;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataManager = new SQLiteDataManager();
        notes = dataManager.loadNotes();
        autoSaver = new AutoSaver(this::autoSave);
        initNotesComboBox();
        initListeners();
        initButtons();
    }

    private void autoSave() {
        Note note = notesComboBox.getValue();
        if (note == null) {
            addNewNote(titleTextField.getText(), mainTextArea.getText());
        } else {
            note.setTitle(titleTextField.getText());
            note.setContent(mainTextArea.getText());
        }
        System.err.println("Auto saved");
    }

    private void initButtons() {
        closeButton.setOnAction(event -> {
            dataManager.saveNotes(notes);
            App.closeApp();
        });
        addNoteButton.setOnAction(event -> addNewNote("", ""));
        deleteNoteButton.setOnAction(event -> deleteCurrentNote());
    }

    private void initListeners() {
        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> autoSaver.contentChanged());
        mainTextArea.textProperty().addListener((observable, oldValue, newValue) -> autoSaver.contentChanged());
    }

    private void initNotesComboBox() {
        notesComboBox.setItems(notes);
        notesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> showNote());
    }

    private void addNewNote(String title, String content) {
        Note note = new Note(title, content);
        notes.add(note);
        notesComboBox.getSelectionModel().select(note);
        System.err.println("added new note and selected it");
    }

    private void clearFields() {
        titleTextField.clear();
        mainTextArea.clear();
        notesComboBox.getSelectionModel().clearSelection();
        System.err.println("cleared note");
    }

    private void deleteCurrentNote() {
        notes.remove(notesComboBox.getValue());
        clearFields();
    }

    private void showNote() {
        Note note = notesComboBox.getValue();
        if (note != null) {
            titleTextField.setText(note.getTitle());
            mainTextArea.setText(note.getContent());
        }
    }
}
