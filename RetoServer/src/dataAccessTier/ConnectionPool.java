package dataAccessTier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private List<Connection> availableConnections = new ArrayList<>();
    private List<Connection> usedConnections = new ArrayList<>();
    private int maxConnections;

    public ConnectionPool(String dbUrl, String dbUser, String dbPassword, int maxConnections) {
        this.maxConnections = maxConnections;
        for (int i = 0; i < maxConnections; i++) {
            availableConnections.add(createConnection(dbUrl, dbUser, dbPassword));
        }
    }

    private Connection createConnection(String dbUrl, String dbUser, String dbPassword) {
        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException("Error creando conexión a la base de datos", e);
        }
    }

    public synchronized Connection getConnection() {
        if (availableConnections.isEmpty()) {
            throw new RuntimeException("No hay conexiones disponibles");
        }
        Connection connection = availableConnections.remove(availableConnections.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        usedConnections.remove(connection);
        availableConnections.add(connection);
    }
}
