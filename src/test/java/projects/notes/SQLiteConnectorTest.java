package projects.notes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLiteConnectorTest {

    private static final String URL = "jdbc:sqlite:database.db";
    private Connection connection;

    @BeforeEach
    public void setUp() {
        // Initializing test data.
        connection = SQLiteConnector.connect(URL);
    }

    @AfterEach
    public void tearDown() {
        // Cleaning up resources.
        SQLiteConnector.disconnect(connection);
    }

    @Test
    public void testConnect() {
        assertNotNull(connection, "Connection should not be null");
        assertTrue(isConnected(connection), "Connection should be open");
    }

    @Test
    public void testDisconnect() {
        SQLiteConnector.disconnect(connection);
        assertFalse(isConnected(connection), "Connection should be closed");
    }

    @Test
    public void testDisconnectWithNullConnection() {
        assertDoesNotThrow(() -> SQLiteConnector.disconnect(null), "Disconnecting null connection should not throw an exception");
    }

    private boolean isConnected(Connection connection) {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking connection status");
        }
    }
}
