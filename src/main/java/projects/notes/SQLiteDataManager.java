package projects.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SQLiteDataManager implements DataManager {

    public static final String INSERT_NOTES_TITLE_CONTENT_VALUES = "INSERT INTO notes(title,content) Values(?,?)";
    public static final String SELECT_FROM_NOTES = "SELECT * FROM notes";
    public static final String CREATE_TABLE_IF_NOT_EXISTS_NOTES_TITLE_CONTENT = "CREATE TABLE IF NOT EXISTS notes(title TEXT, content TEXT)";
    @SuppressWarnings("SqlWithoutWhere")
    public static final String DELETE_FROM_NOTES = "DELETE FROM notes";
    private static final String URL = "jdbc:sqlite:database.db";
    private final Connection connection;

    public SQLiteDataManager() {
        connection = SQLiteConnector.connect(URL);
        createTable();
    }

    @Override
    public void saveNotes(ObservableList<Note> notes) {
        try {
//            Clear existing data
            clearTable();

//            Save new data
            try (PreparedStatement statement = connection.prepareStatement(INSERT_NOTES_TITLE_CONTENT_VALUES)) {
                for (Note note : notes) {
                    statement.setString(1, note.getTitle());
                    statement.setString(2, note.getContent());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error in saving notes: ", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public ObservableList<Note> loadNotes() {
        ObservableList<Note> notes = FXCollections.observableArrayList();
        try {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_FROM_NOTES)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    notes.add(new Note(title, content));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in loading data from database: " + e.getMessage());
        }
        return notes;
    }

    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_IF_NOT_EXISTS_NOTES_TITLE_CONTENT);
        } catch (SQLException e) {
            System.err.println("Error in creating table: " + e.getMessage());
        }
    }

    private void clearTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE_FROM_NOTES);
        } catch (SQLException e) {
            System.err.println("Error in clearing table: " + e.getMessage());
        }
    }

    public void closeConnection() {
        SQLiteConnector.disconnect(connection);
    }
}
