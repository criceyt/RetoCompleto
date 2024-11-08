package dataAccessTier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;

/**
 * Clase que implementa un Pool de conexiones a una base de datos. El objetivo
 * de esta clase es gestionar un conjunto de conexiones a la base de datos para
 * evitar la sobrecarga de abrir y cerrar conexiones repetidamente. Permite
 * obtener y devolver conexiones de manera eficiente.
 *
 * El Pool de Conexiones gestiona un conjunto de conexiones libres y ocupadas.
 * Cuando se solicita una conexión, si hay alguna libre, se toma del conjunto de
 * conexiones libres, si no, se crea una nueva conexión (hasta alcanzar el
 * tamaño máximo de conexiones).
 *
 * @author oier
 */
public class PoolConexiones {

    // Atributos necesarios para gestionar la conexión a la base de datos
    private ResourceBundle fichConf;
    private String databaseUrl;
    private String userName;
    private String password;
    private int maxPoolSize = 10;
    private int connNum = 0;

    // Consulta para verificar si la conexión está activa
    private static final String SQL_VERIFYCONN = "select 1";

    // Pilas de conexiones libres y conjunto de conexiones ocupadas
    Stack<Connection> conexionesLibres = new Stack<>();
    Set<Connection> conexionesOcupadas = new HashSet<>();

    /**
     * Constructor de la clase PoolConexiones, que configura el pool con los
     * parámetros definidos en un archivo de propiedades.
     *
     * @param databaseUrl URL de la base de datos.
     * @param userName Nombre de usuario para la conexión.
     * @param password Contraseña para la conexión.
     * @param maxSize Tamaño máximo del pool de conexiones.
     * @throws SQLException Si no se puede cargar la configuración o establecer
     * la conexión.
     */
    public PoolConexiones(String databaseUrl, String userName,
            String password, int maxSize) throws SQLException {
        try {
            fichConf = ResourceBundle.getBundle("dataAccessTier.conexion"); // Cargar configuración desde el archivo
            databaseUrl = fichConf.getString("url");
            userName = fichConf.getString("user");
            password = fichConf.getString("password");
        } catch (MissingResourceException e) {
            System.err.println("Error: El archivo de propiedades no se pudo encontrar.");
            throw new SQLException("No se pudo cargar la configuración de la base de datos.", e);
        }

        this.maxPoolSize = maxSize; // Asignar el tamaño máximo del pool
    }

    /**
     * Constructor que utiliza configuraciones predeterminadas de un archivo de
     * propiedades.
     *
     * @throws SQLException Si no se puede cargar la configuración o establecer
     * la conexión.
     */
    public PoolConexiones() throws SQLException {
        try {
            fichConf = ResourceBundle.getBundle("dataAccessTier.conexion"); // Cargar configuración desde el archivo
            databaseUrl = fichConf.getString("url");
            userName = fichConf.getString("user");
            password = fichConf.getString("password");
        } catch (MissingResourceException e) {
            System.err.println("Error: El archivo de propiedades no se pudo encontrar.");
            throw new SQLException("No se pudo cargar la configuración de la base de datos.", e);
        }

        this.maxPoolSize = 10; // Tamaño máximo predeterminado del pool
    }

    /**
     * Obtiene una conexión del pool. Si el pool está lleno, crea una nueva
     * conexión.
     *
     * Si no hay conexiones libres, se crea una nueva conexión hasta alcanzar el
     * tamaño máximo del pool. Si el pool está lleno, lanza una excepción.
     *
     * Además, verifica si la conexión obtenida está activa. Si no lo está, la
     * reconecta.
     *
     * @return Una conexión activa.
     * @throws SQLException Si no se puede obtener una conexión del pool o crear
     * una nueva.
     */
    public synchronized Connection getConnection() throws SQLException {
        Connection conn = null;

        // Verificar si el pool está lleno
        if (isFull()) {
            throw new SQLException("La PoolConnexion está llena");
        }

        // Intentar obtener una conexión del pool
        conn = getConnectionFromPool();

        // Si no hay conexiones libres, crear una nueva
        if (conn == null) {
            conn = createNewConnectionForPool();
        }

        // Verificar que la conexión esté disponible
        conn = makeAvailable(conn);
        return conn;
    }

    /**
     * Devuelve una conexión al pool.
     *
     * Verifica que la conexión haya sido obtenida del pool antes de devolverla.
     * Si no es así, lanza una excepción.
     *
     * @param conn La conexión a devolver al pool.
     * @throws SQLException Si la conexión no pertenece al pool o ya ha sido
     * devuelta.
     */
    public synchronized void returnConnection(Connection conn) throws SQLException {
        if (conn == null) {
            throw new NullPointerException();
        }
        // Verificar si la conexión está ocupada y se puede devolver
        if (!conexionesOcupadas.remove(conn)) {
            throw new SQLException("La conexión ya ha sido retornada o no es para esta pool");
        }
        // Colocar la conexión de nuevo en el pool de conexiones libres
        conexionesLibres.push(conn);
    }

    /**
     * Verifica si el pool de conexiones está lleno.
     *
     * El pool se considera lleno si no hay conexiones libres y ya se ha
     * alcanzado el número máximo de conexiones permitidas.
     *
     * @return true si el pool está lleno, false en caso contrario.
     */
    private synchronized boolean isFull() {
        return ((conexionesLibres.size() == 0) && (connNum >= maxPoolSize));
    }

    /**
     * Crea una nueva conexión para el pool si es necesario.
     *
     * Esta función incrementa el número de conexiones actuales y añade la nueva
     * conexión al conjunto de conexiones ocupadas.
     *
     * @return La nueva conexión creada.
     * @throws SQLException Si no se puede crear una nueva conexión.
     */
    private Connection createNewConnectionForPool() throws SQLException {
        Connection conn = createNewConnection(); // Crear una nueva conexión
        connNum++; // Incrementar el contador de conexiones
        conexionesOcupadas.add(conn); // Añadirla al conjunto de conexiones ocupadas
        return conn;
    }

    /**
     * Crea una nueva conexión a la base de datos utilizando los parámetros de
     * configuración.
     *
     * @return La nueva conexión creada.
     * @throws SQLException Si no se puede crear una nueva conexión.
     */
    private Connection createNewConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(databaseUrl, userName, password); // Crear la conexión
        return conn;
    }

    /**
     * Obtiene una conexión libre del pool.
     *
     * Si el pool tiene conexiones libres, las obtiene de la pila de conexiones
     * libres y las agrega al conjunto de conexiones ocupadas.
     *
     * @return La conexión obtenida del pool.
     */
    private Connection getConnectionFromPool() {
        Connection conn = null;
        if (conexionesLibres.size() > 0) {
            conn = conexionesLibres.pop(); // Sacar una conexión libre del stack
            conexionesOcupadas.add(conn); // Añadirla a las conexiones ocupadas
        }
        return conn;
    }

    /**
     * Verifica que la conexión esté disponible. Si no lo está, la reconecta.
     *
     * Esto es necesario para casos como bases de datos en la nube, donde una
     * conexión puede perderse si no se usa durante un tiempo.
     *
     * @param conn La conexión a verificar.
     * @return La conexión disponible.
     * @throws SQLException Si no se puede recuperar una conexión disponible.
     */
    private Connection makeAvailable(Connection conn) throws SQLException {
        if (isConnectionAvailable(conn)) {
            return conn; // Si la conexión está disponible, retornarla
        }

        // Si la conexión no está disponible, reconectarla
        conexionesOcupadas.remove(conn);
        connNum--;
        conn.close();

        conn = createNewConnection(); // Crear una nueva conexión
        conexionesOcupadas.add(conn); // Añadirla a las conexiones ocupadas
        connNum++;
        return conn;
    }

    /**
     * Verifica si una conexión está activa y disponible para su uso.
     *
     * Realiza una consulta SQL simple para verificar la disponibilidad de la
     * conexión.
     *
     * @param conn La conexión a verificar.
     * @return true si la conexión está activa, false en caso contrario.
     */
    private boolean isConnectionAvailable(Connection conn) {
        try (Statement st = conn.createStatement()) {
            st.executeQuery(SQL_VERIFYCONN); // Ejecutar la consulta para verificar la conexión
            return true; // Si la consulta tiene éxito, la conexión está disponible
        } catch (SQLException e) {
            return false; // Si ocurre un error, la conexión no está disponible
        }
    }
}
