package projects.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class FileDataManager implements DataManager {

    private static final String NOTES_PATH = "src/main/resources/save.txt";
    private boolean saveFileAlreadyExists;
    private File saveFile;

    public FileDataManager() {
        initSaveFile();
    }

    private void initSaveFile() {
        saveFile = new File(NOTES_PATH);
        try {
            if (saveFile.createNewFile()) {
                System.err.println("New File created");
                saveFileAlreadyExists = false;
            } else {
                System.err.println("File already exists");
                saveFileAlreadyExists = true;
            }
        } catch (IOException io) {
            System.err.println("File path error");
        }
    }

    @Override
    public void saveNotes(ObservableList<Note> notes) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            ArrayList<Note> noteArrayList = new ArrayList<>(notes);
            outputStream.writeObject(noteArrayList);
            System.err.println("Saved file successfully");
        } catch (IOException io) {
            System.err.println("Failed saving file\n" + io.getMessage());
        }
    }

    @Override
    public ObservableList<Note> loadNotes() {
        if (saveFileAlreadyExists) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(saveFile))) {
                //noinspection unchecked
                ArrayList<Note> noteArrayList = (ArrayList<Note>) inputStream.readObject();
                System.err.println("Loaded file successfully");
                return FXCollections.observableArrayList(noteArrayList);
            } catch (IOException io) {
                System.err.println("Failed loading file\n" + io.getMessage());
            } catch (ClassNotFoundException ce) {
                System.err.println("Error in reading the class from file\n" + ce.getMessage());
            }
        }
        return FXCollections.observableArrayList();
    }

}
