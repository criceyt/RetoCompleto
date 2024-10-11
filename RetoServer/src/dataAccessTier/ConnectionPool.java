/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccessTier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 2dam
 */
public class ConnectionPool {
    private List<Connection> connectionPool;
    private String url;
    private String user;
    private String password;
    private int maxConnections;

    public ConnectionPool(String url, String user, String password, int maxConnections) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.maxConnections = maxConnections;
        connectionPool = new ArrayList<>(maxConnections);
        createConnections();
    }

    private void createConnections() {
        for (int i = 0; i < maxConnections; i++) {
            try {
                Connection connection = DriverManager.getConnection(url, user, password);
                connectionPool.add(connection);
            } catch (SQLException e) {
                e.printStackTrace(); // Manejar el error según sea necesario
            }
        }
    }

    public synchronized Connection getConnection() {
        if (!connectionPool.isEmpty()) {
            return connectionPool.remove(0);
        }
        return null; // O lanzar una excepción
    }

    public synchronized void releaseConnection(Connection connection) {
        connectionPool.add(connection);
    }
}