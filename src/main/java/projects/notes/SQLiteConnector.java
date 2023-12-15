package projects.notes;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {

    public static Connection connect(String databaseUrl) {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(databaseUrl);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC class not found: ", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database: ", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected Error occurred: ", e);
        }
    }

    public static void disconnect(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error disconnecting the database: ", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected Error occurred: ", e);
        }
    }
}
