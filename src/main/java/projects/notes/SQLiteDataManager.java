package projects.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SQLiteDataManager implements DataManager {

    private final Connection connection;

    public SQLiteDataManager() {
        connection = SQLiteConnector.connect();
        createTable();
    }

    @Override
    public void saveNotes(ObservableList<Note> notes) {
        try {
//            Clear existing data
            clearTable();

//            Save new data
            String sql = "INSERT INTO notes(title,content) Values(?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Note note : notes) {
                    statement.setString(1, note.getTitle());
                    statement.setString(2, note.getContent());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in saving notes " + e.getMessage());
        }
    }

    @Override
    public ObservableList<Note> loadNotes() {
        ObservableList<Note> notes = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM notes";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    notes.add(new Note(title, content));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in loading data from database " + e.getMessage());
        }
        return notes;
    }

    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS notes(title TEXT, content TEXT)";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error in creating table " + e.getMessage());
        }
    }

    private void clearTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM notes";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error in clearing table " + e.getMessage());
        }
    }

    public void closeConnection() {
        SQLiteConnector.disconnect(connection);
    }
}
