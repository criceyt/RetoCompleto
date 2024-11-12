package dataAccessTier;

import exceptions.ErrorCorreoExistente;
import exceptions.ErrorGeneral;
import exceptions.ErrorMaxClientes;
import exceptions.ErrorUsuarioInexistente;
import exceptions.ErrorUsuarioNoActivo;
import exceptions.NoConnectionsAvailableException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Mensaje;
import libreria.Request;
import libreria.Signable;
import libreria.Usuario;

/**
 * Clase de acceso a datos que implementa la interfaz Signable para gestionar la
 * autenticación y el registro de usuarios. Conecta con la base de datos para
 * insertar usuarios y verificar sus credenciales.
 */
public class DAO implements Signable {

    private static final Logger LOGGER = Logger.getLogger(AplicattionServer.class.getName());

    private PoolConexionNuevo pool; // Pool de conexiones a la base de datos

    // Variables para la conexión y las consultas SQL
    private Connection con;
    private PreparedStatement stmt;

    private ResourceBundle fichConf;
    private String url;
    private String usuario;
    private String pass;

    // Consultas SQL
    private final String altaParner = "INSERT INTO res_partner (company_id, name, email, street, city, zip, mobile) VALUES (1, ?, ?, ?, ?, ?, ?)";
    private final String altaUsers = "INSERT INTO res_users (company_id, partner_id, login, password, active, notification_type) VALUES (1, ?, ?, ?, ?, 'email')";
    private final String selectParnerId = "SELECT id FROM res_partner order by id desc limit 1";
    private final String comprobarEmail = "SELECT email FROM res_partner WHERE email=?";
    private final String inicioSesion = "SELECT company_id, partner_id, login, password, active FROM res_users WHERE login=? AND password=?";
    private final String estaActivo = "SELECT active FROM res_users WHERE login=? AND password=?";

    /**
     * Constructor de la clase DAO. Inicializa el pool de conexiones a la base de datos.
     *
     * @throws SQLException Si hay un error al establecer la conexión a la base de datos.
     */
    public DAO() throws SQLException {
        this.pool = new PoolConexionNuevo(); // Crear un nuevo pool de conexiones con la clase PoolConexionNuevo
    }

    /**
     * Método que asegura que se cierre la conexión con la base de datos y los recursos.
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

    @Override
    public synchronized Usuario signIn(Mensaje mensaje) throws ErrorGeneral, ErrorUsuarioNoActivo, ErrorUsuarioInexistente {
        String email = mensaje.getUser().getEmail();
        String password = mensaje.getUser().getPassword();
        ResultSet rs = null;

        try {
            con = pool.getConnection(); 
            stmt = con.prepareStatement(inicioSesion);
            stmt.setString(1, email);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                boolean isActive = rs.getBoolean("active");

                if (isActive) {
                    mensaje.setRq(Request.SIGN_IN_EXITOSO);
                } else {
                    throw new ErrorUsuarioNoActivo();
                }
            } else {
                throw new ErrorUsuarioInexistente();
            }

            conexionRealizada(); // Cerrar la conexión y liberar los recursos

        } catch (SQLException ex) {
            throw new ErrorGeneral();
        } catch (NoConnectionsAvailableException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje.getUser();
    }


    @Override
    public Usuario singUp(Mensaje mensaje) throws ErrorGeneral, ErrorCorreoExistente, ErrorMaxClientes {
               try {
            con = pool.getConnection(); 
            stmt = con.prepareStatement(comprobarEmail);
            stmt.setString(1, mensaje.getUser().getEmail());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                throw new ErrorCorreoExistente();
            } else {
                stmt = con.prepareStatement(altaParner);
                stmt.setString(1, mensaje.getUser().getNombreyApellidos());
                stmt.setString(2, mensaje.getUser().getEmail());
                stmt.setString(3, mensaje.getUser().getDireccion());
                stmt.setString(4, mensaje.getUser().getCiudad());
                stmt.setInt(5, mensaje.getUser().getCodigoPostal());
                stmt.setInt(6, mensaje.getUser().getMobile()); 
                stmt.executeUpdate();

                stmt = con.prepareStatement(selectParnerId);
                ResultSet rs2 = stmt.executeQuery();
                int idBuscado = 0;
                if (rs2.next()) {
                    idBuscado = rs2.getInt("id");
                }

                stmt = con.prepareStatement(altaUsers);
                stmt.setInt(1, idBuscado);
                stmt.setString(2, mensaje.getUser().getEmail());
                stmt.setString(3, mensaje.getUser().getPassword());
                stmt.setBoolean(4, mensaje.getUser().isEstaActivo());
                stmt.executeUpdate();

                conexionRealizada(); 
            }

        } catch (SQLException e) {
            throw new ErrorGeneral();
        } catch (NoConnectionsAvailableException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mensaje.getUser();
    }
}
