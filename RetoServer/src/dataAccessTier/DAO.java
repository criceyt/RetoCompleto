package dataAccessTier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import libreria.Signable;
import libreria.Usuario;

public class DAO implements Signable {

    private PoolConexiones pool;

    // HERREMIENTAS
    private Connection con;
    private PreparedStatement stmt;

    private ResourceBundle fichConf;
    private String url;
    private String usuario;
    private String pass;

    // Alta a la tabla parthner
    private final String altaParner = "INSERT INTO res_partner (company_id, name, email, street, city, zip) VALUES (1, ?, ?, ?, ?, ?)";
    // Alta a la tabla user
    private final String altaUsers = "INSERT INTO res_users (company_id, partner_id, login, password, active, notification_type) VALUES (1, ?, ?, ?, ?, 'email')";
    // Comprobar si el email esta bien
    private final String comprobarEmail = "SELECT email FROM res_partner WHERE email=?";
    // Buscar el id para agregar a res_user
    private final String selectParthnerId = "select id from res_partner order by id desc limit 1";
    // Inicio de sesion
    private final String inicioSesion = "SELECT company_id, partner_id, login, password, active, notification_type FROM res_users WHERE login=? AND passsword=?";

    public DAO() {
        fichConf = ResourceBundle.getBundle("libreria.config");
        url = fichConf.getString("url");
        usuario = fichConf.getString("user");
        pass = fichConf.getString("password");
    }

    private void releaseConnection() throws SQLException {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                pool.returnConnection(con);
            }

        } catch (SQLException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void singUp(Usuario usuario) {

        // Se inserta la Primera Parte que corresponde ALTA_PARNER
        try {
        // Se abre conexion con postgres
            pool = new PoolConexiones();
            con = pool.getConnection();

            // Primero hay que comprobar que no esta introducido el email
            stmt = con.prepareStatement(comprobarEmail);
            stmt.setString(1, usuario.getEmail());
            ResultSet rs;
            rs = stmt.executeQuery();

            // Hacemos una extepcion de correo
            if (rs.next()) {
            // mensaje de error CORREO REPE
            } else {
            // PRIMERA TABLA
                stmt = con.prepareStatement(altaParner);

                stmt.setString(1, usuario.getNombreyApellidos());
                stmt.setString(2, usuario.getEmail());
                stmt.setString(3, usuario.getDireccion());
                stmt.setString(4, usuario.getCiudad());
                stmt.setInt(5, usuario.getCodigoPostal());
                stmt.executeUpdate();

                // Se busca el Parner id y lo implementamos en el alta
                stmt = con.prepareStatement(selectParthnerId);
                ResultSet rs2 = stmt.executeQuery();
                int idBuscado = 0;
                if (rs2.next()) {
                    idBuscado = rs2.getInt("id");
                }

                // SEGUNDA TABLA
                stmt = con.prepareStatement(altaUsers);

                stmt.setInt(1, idBuscado);
                stmt.setString(2, usuario.getEmail());
                stmt.setString(3, usuario.getPassword());
                stmt.setBoolean(4, usuario.isEstaActivo());
                stmt.executeUpdate();

                stmt.executeUpdate();

                releaseConnection();

            }

        } catch (SQLException e) {
            // ERROR GENERAL
            e.printStackTrace();
        }
    }
}
