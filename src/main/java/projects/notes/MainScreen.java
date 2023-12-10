package projects.notes;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {

    private static final String NOTES_PATH = "src/main/resources/save.txt";
    public TextArea mainTextArea;
    public Button closeButton;
    public TextField titleTextField;
    private File saveFile;
    private boolean saveFileAlreadyExists;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeButton.setOnAction(event -> {
            saveNotes();
            App.closeApp();
        });
        initSaveFile();
        loadNotes();
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
                Note note = (Note) inputStream.readObject();
                titleTextField.setText(note.getTitle());
                mainTextArea.setText(note.getDescription());
            } catch (IOException io) {
                System.err.println("Failed in locating file while loading\n" + io.getMessage());
            } catch (ClassNotFoundException ce) {
                System.err.println("Error in reading the class from file\n" + ce.getMessage());
            }
        }
    }

    private void saveNotes() {
//        TODO: change method to allow to append to file
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            Note note = new Note(titleTextField.getText(), mainTextArea.getText());
            outputStream.writeObject(note);
            System.out.println("Saved file successfully");
        } catch (IOException io) {
            System.out.println("Failed in locating file while saving\n" + io.getMessage());
        }

    }
}
