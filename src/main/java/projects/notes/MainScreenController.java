package projects.notes;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    private final DataManager dataManager;
    public TextArea mainTextArea;
    public Button closeButton;
    public TextField titleTextField;
    public Button deleteNoteButton;
    public Button addNoteButton;
    public ComboBox<Note> notesComboBox;
    private ObservableList<Note> notes;
    private AutoSaver autoSaver;

    public MainScreenController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupUI();
        setupEventHandlers();
        loadNotes();
    }

    private void setupUI() {
        notes = dataManager.loadNotes();
        notesComboBox.setItems(notes);
        notesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> showNoteDetails());
    }

    private void setupEventHandlers() {
        autoSaver = new AutoSaver(this::autoSave);
        closeButton.setOnAction(event -> onCloseButtonClicked());
        addNoteButton.setOnAction(event -> addNewNote("", ""));
        deleteNoteButton.setOnAction(event -> deleteCurrentNote());
        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> autoSaver.contentChanged());
        mainTextArea.textProperty().addListener((observable, oldValue, newValue) -> autoSaver.contentChanged());
    }

    private void loadNotes() {
        notes.clear();
        notes.addAll(dataManager.loadNotes());
    }

    private void autoSave() {
        Note selectedNote = notesComboBox.getValue();
        if (selectedNote == null) {
            addNewNote(titleTextField.getText(), mainTextArea.getText());
        } else {
            selectedNote.setTitle(titleTextField.getText());
            selectedNote.setContent(mainTextArea.getText());
        }
        System.err.println("Auto saved");
    }

    private void onCloseButtonClicked() {
        autoSave();
        dataManager.saveNotes(notes);
        App.closeApp();
    }

    private void showNoteDetails() {
        Note selectedNote = notesComboBox.getValue();
        if (selectedNote != null) {
            titleTextField.setText(selectedNote.getTitle());
            mainTextArea.setText(selectedNote.getContent());
        }
    }

    private void addNewNote(String title, String content) {
        Note newNote = new Note(title, content);
        notes.add(newNote);
        notesComboBox.getSelectionModel().select(newNote);
        System.err.println("Added new note and selected it");
    }

    private void deleteCurrentNote() {
        Note selectedNote = notesComboBox.getValue();
        notes.remove(selectedNote);
        clearFields();
    }

    private void clearFields() {
        titleTextField.clear();
        mainTextArea.clear();
        notesComboBox.getSelectionModel().clearSelection();
        System.err.println("Cleared note");
    }
}