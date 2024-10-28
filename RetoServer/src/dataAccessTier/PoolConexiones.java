package dataAccessTier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author oier
 */
public class PoolConexiones {

    // HERRAMIENTAS NECESARIAS
    private ResourceBundle fichConf;
    private String databaseUrl;
    private String userName;
    private String password;
    private int maxPoolSize = 10;
    private int connNum = 0;

    // Consulta para verificar conexion
    private static final String SQL_VERIFYCONN = "select 1";

    Stack<Connection> conexionesLibres = new Stack<>();
    Set<Connection> conexionesOcupadas = new HashSet<>();

    // Constructor de la Pool
    public PoolConexiones(String databaseUrl, String userName,
            String password, int maxSize) throws SQLException {
        try {
            fichConf = ResourceBundle.getBundle("dataAccessTier.conexion");
            databaseUrl = fichConf.getString("url");
            userName = fichConf.getString("user");
            password = fichConf.getString("password");
        } catch (MissingResourceException e) {
            System.err.println("Error: El archivo de propiedades no se pudo encontrar.");
            throw new SQLException("No se pudo cargar la configuración de la base de datos.", e);
        };

        this.maxPoolSize = maxSize;
    }  
    
    public PoolConexiones() throws SQLException {
        try {
            fichConf = ResourceBundle.getBundle("dataAccessTier.conexion");
            databaseUrl = fichConf.getString("url");
            userName = fichConf.getString("user");
            password = fichConf.getString("password");
        } catch (MissingResourceException e) {
            System.err.println("Error: El archivo de propiedades no se pudo encontrar.");
            throw new SQLException("No se pudo cargar la configuración de la base de datos.", e);
        };

        this.maxPoolSize = 10;
    }

    // Creamos una conexion 
    public synchronized Connection getConnection() throws SQLException {
        Connection conn = null;

        if (isFull()) {
            throw new SQLException("La PoolConnexion esta llena");
        }

        conn = getConnectionFromPool();

        // If there is no free connection, create a new one.
        if (conn == null) {
            conn=createNewConnectionForPool();
        }

        // For Azure Database for MySQL, if there is no action on one connection for some
        // time, the connection is lost. By this, make sure the connection is
        // active. Otherwise reconnect it.
        conn = makeAvailable(conn);
        return conn;
    }

    /**
     * Return a connection to the pool
     *
     * @param conn The connection
     * @throws SQLException When the connection is returned already or it isn't
     * gotten from the pool.
     */
    public synchronized void returnConnection(Connection conn)
            throws SQLException {
        if (conn == null) {
            throw new NullPointerException();
        }
        if (!conexionesOcupadas.remove(conn)) {
            throw new SQLException(
                    "La conexion ya ha sido retornada o no es para esta pool");
        }
        conexionesLibres.push(conn);
    }

    /**
     * Verify if the connection is full.
     *
     * @return if the connection is full
     */
    private synchronized boolean isFull() {
        return ((conexionesLibres.size() == 0) && (connNum >= maxPoolSize));
    }

    /**
     * Create a connection for the pool
     *
     * @return the new created connection
     * @throws SQLException When fail to create a new connection.
     */
    private Connection createNewConnectionForPool() throws SQLException {
        Connection conn=createNewConnection();
        connNum++;
        conexionesOcupadas.add(conn);
        return conn;
    }

    /**
     * Crate a new connection
     *
     * @return the new created connection
     * @throws SQLException When fail to create a new connection.
     */
    private Connection createNewConnection() throws SQLException {
        Connection conn=null;
        conn = DriverManager.getConnection(databaseUrl, userName, password);
        return conn;
    }

    // Metodo para obtener la conexion de la POOL bb
    private Connection getConnectionFromPool() {
        Connection conn = null;
        if (conexionesLibres.size() > 0) {
            conn = conexionesLibres.pop();
            conexionesOcupadas.add(conn);
        }
        return conn;
    }

    /**
     * Make sure the connection is available now. Otherwise, reconnect it.
     *
     * @param conn The connection for verification.
     * @return the available connection.
     * @throws SQLException Fail to get an available connection
     */
    private Connection makeAvailable(Connection conn) throws SQLException {
        if (isConnectionAvailable(conn)) {
            return conn;
        }

        // If the connection is't available, reconnect it.
        conexionesOcupadas.remove(conn);
        connNum--;
        conn.close();

        conn=createNewConnection();
        conexionesOcupadas.add(conn);
        connNum++;
        return conn;
    }

    /**
     * By running a sql to verify if the connection is available
     *
     * @param conn The connection for verification
     * @return if the connection is available for now.
     */
    private boolean isConnectionAvailable(Connection conn) {
        try (Statement st = conn.createStatement()) {
            st.executeQuery(SQL_VERIFYCONN);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Just an Example
    public static void main(String[] args) throws SQLException {
        PoolConexiones pool = new PoolConexiones(
                "jdbc:postgresql://192.168.37.155:5432/OdooDB",
                "odoo", "abcd*1234", 2);

        try (Connection conn = pool.getConnection();
                Statement statement = conn.createStatement()) {

            ResultSet res = statement.executeQuery("SELECT table_name FROM information_schema.tables");
            while (res.next()) {
                String tblName = res.getString(1);
                System.out.println(tblName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
