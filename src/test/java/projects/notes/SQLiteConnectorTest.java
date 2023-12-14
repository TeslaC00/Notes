package projects.notes;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteConnectorTest {

    private static Connection connection;

    @BeforeAll
    static void setUp() {
//        Establish connection before running tests
        connection = SQLiteConnector.connect();
    }

    @AfterAll
    static void tearDown() {
//        Close connection after running tests
        SQLiteConnector.disconnect(connection);
    }

    @Test
    void testDatabaseConnection() {
        assertNotNull(connection, "Connection should be not null");
        try {
            assertFalse(connection.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("Exception occurred while checking connection status: " + e.getMessage());
        }
    }

    @Test
    void testDatabaseDisconnect() {
//        Ensure that disconnect closes the connection
        SQLiteConnector.disconnect(connection);
        try {
            assertTrue(connection.isClosed(), "Connection should be closed");
        } catch (SQLException e) {
            fail("Exception occurred while closing the connection: " + e.getMessage());
        }
    }
}