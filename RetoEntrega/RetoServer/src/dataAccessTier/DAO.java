package dataAccessTier;

import exceptions.ErrorCorreoExistente;
import exceptions.ErrorGeneral;
import exceptions.ErrorUsuarioInexistente;
import exceptions.ErrorUsuarioNoActivo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import libreria.Mensaje;
import libreria.Request;
import libreria.Signable;
import libreria.Usuario;

/**
 * Clase de acceso a datos que implementa la interfaz Signable para gestionar la
 * autenticación y el registro de usuarios. Conecta con la base de datos para
 * insertar usuarios y verificar sus credenciales.
 *
 * @author gorka
 */
public class DAO implements Signable {

    private static final Logger LOGGER = Logger.getLogger(AplicattionServer.class.getName());

    private PoolConexiones pool; // Pool de conexiones a la base de datos

    // Variables para la conexión y las consultas SQL
    private Connection con;
    private PreparedStatement stmt;

    private ResourceBundle fichConf;
    private String url;
    private String usuario;
    private String pass;

    // Consultas SQL
    private final String altaParner = "INSERT INTO res_partner (company_id, name, email, street, city, zip) VALUES (1, ?, ?, ?, ?, ?)";
    private final String altaUsers = "INSERT INTO res_users (company_id, partner_id, login, password, active, notification_type) VALUES (1, ?, ?, ?, ?, 'email')";
    private final String selectParnerId = "SELECT id FROM res_partner order by id desc limit 1";
    private final String comprobarEmail = "SELECT email FROM res_partner WHERE email=?";
    private final String inicioSesion = "SELECT company_id, partner_id, login, password, active FROM res_users WHERE login=? AND password=?";
    private final String estaActivo = "SELECT active FROM res_users WHERE login=? AND password=?";

    /**
     * Constructor de la clase DAO. Inicializa el pool de conexiones a la base
     * de datos.
     *
     * @throws SQLException Si hay un error al establecer la conexión a la base
     * de datos.
     */
    public DAO() throws SQLException {
        this.pool = new PoolConexiones(); // Crear un nuevo pool de conexiones
    }

    /**
     * Método que asegura que se cierre la conexión con la base de datos y los
     * recursos.
     *
     * @throws SQLException Si hay un error al cerrar las conexiones.
     */
    private void conexionRealizada() throws SQLException {
        try {
            // Cerrar la sentencia SQL si está abierta
            if (stmt != null) {
                stmt.close();
            }
            // Devolver la conexión al pool si está abierta
            if (con != null) {
                pool.returnConnection(con);
            }

        } catch (SQLException e) {
            LOGGER.info("No se ha realizado la conexion con exito");
        }
    }

    /**
     * Método que gestiona el inicio de sesión de un usuario. Verifica las
     * credenciales y si el usuario está activo. Si el inicio de sesión es
     * exitoso, retorna el usuario autenticado.
     *
     * @param mensaje Objeto que contiene el usuario con las credenciales de
     * inicio de sesión.
     * @return Usuario autenticado.
     * @throws ErrorGeneral Si ocurre un error general durante la operación.
     * @throws ErrorUsuarioNoActivo Si el usuario no está activo.
     * @throws ErrorUsuarioInexistente Si el usuario no existe en la base de
     * datos.
     */
    @Override
    public synchronized Usuario signIn(Mensaje mensaje) throws ErrorGeneral, ErrorUsuarioNoActivo, ErrorUsuarioInexistente {
        String email = mensaje.getUser().getEmail();
        String password = mensaje.getUser().getPassword();
        ResultSet rs = null;

        try {
            con = pool.getConnection(); // Obtener una conexión del pool
            stmt = con.prepareStatement(inicioSesion);
            stmt.setString(1, email); // Establecer el email en la consulta
            stmt.setString(2, password); // Establecer la contraseña en la consulta

            rs = stmt.executeQuery();

            if (rs.next()) {
                boolean isActive = rs.getBoolean("active"); // Verificar si el usuario está activo

                if (isActive) {
                    mensaje.setRq(Request.SIGN_IN_EXITOSO); // Si el usuario está activo, el inicio de sesión es exitoso
                } else {
                    System.out.println("El usuario no está activo");
                    throw new ErrorUsuarioNoActivo(); // Lanzar error si el usuario no está activo
                }
            } else {
                System.out.println("ERROR: no coincide o no encontrado");
                throw new ErrorUsuarioInexistente(); // Lanzar error si el usuario no existe
            }

            conexionRealizada(); // Cerrar la conexión y liberar los recursos

        } catch (SQLException ex) {
            throw new ErrorGeneral(); // Lanzar un error general si ocurre una excepción SQL
        }

        return mensaje.getUser(); // Retornar el usuario autenticado
    }

    /**
     * Método que gestiona el registro de un nuevo usuario. Verifica si el
     * correo electrónico ya está registrado y, en caso contrario, inserta los
     * datos del nuevo usuario en la base de datos.
     *
     * @param mensaje Objeto que contiene los datos del nuevo usuario.
     * @return Usuario registrado.
     * @throws ErrorCorreoExistente Si el correo electrónico ya está registrado
     * en la base de datos.
     * @throws ErrorGeneral Si ocurre un error general durante la operación.
     */
    @Override
    public synchronized Usuario singUp(Mensaje mensaje) throws ErrorCorreoExistente, ErrorGeneral {
        try {
            con = pool.getConnection(); // Obtener una conexión del pool
            stmt = con.prepareStatement(comprobarEmail);
            stmt.setString(1, mensaje.getUser().getEmail()); // Establecer el email en la consulta
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                throw new ErrorCorreoExistente(); // Lanzar error indicando que el correo ya está registrado
            } else {
                // Si el correo no existe, proceder con el registro
                stmt = con.prepareStatement(altaParner);
                stmt.setString(1, mensaje.getUser().getNombreyApellidos());
                stmt.setString(2, mensaje.getUser().getEmail());
                stmt.setString(3, mensaje.getUser().getDireccion());
                stmt.setString(4, mensaje.getUser().getCiudad());
                stmt.setInt(5, mensaje.getUser().getCodigoPostal());
                stmt.executeUpdate();

                stmt = con.prepareStatement(selectParnerId); // Preparar la consulta para obtener el ID del nuevo "partner"
                ResultSet rs2 = stmt.executeQuery();
                int idBuscado = 0;
                if (rs2.next()) {
                    idBuscado = rs2.getInt("id"); // Obtener el ID del último "partner" insertado
                }

                stmt = con.prepareStatement(altaUsers);
                stmt.setInt(1, idBuscado); // Asignar el ID del "partner"
                stmt.setString(2, mensaje.getUser().getEmail());
                stmt.setString(3, mensaje.getUser().getPassword());
                stmt.setBoolean(4, mensaje.getUser().isEstaActivo());
                stmt.executeUpdate();

                conexionRealizada(); // Cerrar la conexión y liberar los recursos
            }

        } catch (SQLException e) {
            throw new ErrorGeneral(); // Lanzar error general si ocurre una excepción SQL
        }

        return mensaje.getUser(); // Retornar el usuario registrado
    }
}
