package dataAccessTier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Stack;
import org.apache.commons.dbcp2.BasicDataSource;
import exceptions.NoConnectionsAvailableException; // Asegúrate de importar la excepción personalizada

public class PoolConexionNuevo {
    private BasicDataSource dataSource;
    private Stack<Connection> conexionesLibres = new Stack<>();
    private int maxPoolSize = 5; // Valor predeterminado
    private int connNum = 0;

    /**
     * Constructor que configura el BasicDataSource usando las propiedades de un archivo .properties.
     *
     * @throws SQLException Si no se puede configurar la conexión.
     */
    public PoolConexionNuevo() throws SQLException {
        try {
            // Cargar el archivo de propiedades usando ResourceBundle
            ResourceBundle fichConf = ResourceBundle.getBundle("dataAccessTier.conexion");
            
            // Leer las propiedades desde el archivo de recursos
            String url = fichConf.getString("url");
            String user = fichConf.getString("user");
            String password = fichConf.getString("password");
            String maxConnections = fichConf.getString("maxCon");

            // Configurar el BasicDataSource
            dataSource = new BasicDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            dataSource.setMaxTotal(Integer.parseInt(maxConnections)); // Establecer el límite de conexiones

        } catch (MissingResourceException e) {
            System.err.println("Error: El archivo de propiedades no se pudo encontrar.");
            throw new SQLException("No se pudo cargar la configuración de la base de datos.", e);
        }

        // Inicializar el Stack de conexiones
        conexionesLibres = new Stack<>();
    }

    /**
     * Obtiene una conexión del pool.
     *
     * @return Una conexión disponible.
     * @throws SQLException Si no se puede obtener una conexión.
     * @throws NoConnectionsAvailableException Si no hay conexiones disponibles en el pool.
     */
    public synchronized Connection getConnection() throws SQLException, NoConnectionsAvailableException {
        Connection conn = null;

        // Si el stack tiene conexiones libres, tomamos una
        if (!conexionesLibres.isEmpty()) {
            conn = conexionesLibres.pop();
        } else if (connNum < maxPoolSize) {
            // Si no hay conexiones libres y no hemos alcanzado el límite, creamos una nueva conexión
            conn = dataSource.getConnection();
            connNum++;
        } else {
            // Si no hay conexiones disponibles y hemos alcanzado el máximo, lanzamos una excepción personalizada
            throw new NoConnectionsAvailableException(); // Lanzamos la excepción personalizada
        }

        return conn;
    }

    /**
     * Devuelve una conexión al pool.
     *
     * @param conn La conexión a devolver.
     * @throws SQLException Si ocurre un error al devolver la conexión.
     */
    public synchronized void returnConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conexionesLibres.push(conn);
        }
    }

    /**
     * Cierra todas las conexiones y libera recursos.
     *
     * @throws SQLException Si ocurre un error al cerrar las conexiones.
     */
    public synchronized void close() throws SQLException {
        for (Connection conn : conexionesLibres) {
            conn.close();
        }
        conexionesLibres.clear();
        dataSource.close();
    }
}
