package dataAccessTier;


import exceptions.PoolLlenoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;
import org.apache.commons.dbcp2.BasicDataSource;

public class PoolExamen {

    private BasicDataSource dataSource;
    private Stack<Connection> conexionesLibres = new Stack<>();
    private int maxPoolSize = 5; // Máximo de 5 conexiones en el pool
    private int conexionesEnUso = 0;

    /**
     * Constructor que configura el BasicDataSource y llena el stack de conexiones.
     *
     * @param databaseUrl URL de la base de datos
     * @param userName Nombre de usuario para la base de datos
     * @param password Contraseña de la base de datos
     */
    public PoolExamen(String databaseUrl, String userName, String password) {
        // Verificar que los parámetros no sean nulos
        if (databaseUrl == null || userName == null || password == null) {
            throw new IllegalArgumentException("Los parámetros de la base de datos no pueden ser nulos.");
        }
        
        // Configuración del BasicDataSource
        dataSource = new BasicDataSource();
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        
        // Limitar el BasicDataSource a un máximo de 5 conexiones activas
        dataSource.setMaxTotal(maxPoolSize);

        // Crear y llenar el stack de conexiones usando BasicDataSource
        initializeConnections();
    }

    // Constructor sin parámetros
    PoolExamen() {
        // Inicializar el pool con valores predeterminados
        this("jdbc:postgresql://192.168.37.155:5432/OdooDB", "odoo", "abcd*1234");
    }

    /**
     * Inicializa las conexiones en el pool y las añade al stack de conexiones libres.
     */
    private void initializeConnections() {
        if (dataSource == null) {
            throw new IllegalStateException("El DataSource no ha sido inicializado correctamente.");
        }

        for (int i = 0; i < maxPoolSize; i++) {
            try {
                Connection conn = dataSource.getConnection();
                if (conn != null) {
                    conexionesLibres.push(conn);
                }
            } catch (SQLException e) {
                System.err.println("Error al obtener la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene una conexión del stack de conexiones libres.
     *
     * @return Una conexión disponible
     * @throws PoolLlenoException Si no hay conexiones disponibles en el stack
     */
    public synchronized Connection getConnection() throws PoolLlenoException {
        // Comprobar si el stack de conexiones libres está vacío
        if (conexionesLibres.isEmpty() && conexionesEnUso >= maxPoolSize) {
            throw new PoolLlenoException("No hay conexiones disponibles en el pool.");
        }

        // Si hay conexiones libres, las usamos
        if (!conexionesLibres.isEmpty()) {
            conexionesEnUso++; // Aumentamos el contador de conexiones en uso
            return conexionesLibres.pop();
        }

        // Si no hay conexiones libres, intentamos crear una nueva si no hemos alcanzado el límite
        try {
            if (conexionesEnUso < maxPoolSize) {
                Connection conn = dataSource.getConnection();
                conexionesEnUso++; // Aumentamos el contador de conexiones en uso
                return conn;
            } else {
                throw new PoolLlenoException("No hay conexiones disponibles en el pool.");
            }
        } catch (SQLException e) {
            throw new PoolLlenoException("No se pudo obtener una nueva conexión.");
        }
    }

    /**
     * Devuelve una conexión al stack de conexiones libres.
     *
     * @param conn La conexión a devolver
     * @throws SQLException Si ocurre un error al devolver la conexión
     */
    public synchronized void returnConnection(Connection conn) throws SQLException {
        if (conn == null || conexionesLibres.size() >= maxPoolSize) {
            throw new SQLException("La conexión no se puede devolver al pool.");
        }
        conexionesLibres.push(conn);
        conexionesEnUso--; // Disminuimos el contador de conexiones en uso
    }

    /**
     * Cierra todas las conexiones del stack y el BasicDataSource.
     *
     * @throws SQLException Si ocurre un error al cerrar las conexiones
     */
    public void closePool() throws SQLException {
        while (!conexionesLibres.isEmpty()) {
            Connection conn = conexionesLibres.pop();
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
        if (dataSource != null) {
            dataSource.close();
        }
    }
}

