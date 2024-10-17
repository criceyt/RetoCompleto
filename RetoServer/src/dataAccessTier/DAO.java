package dataAccessTier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    // CONSULTAS:
    private final String altaParner = "INSERT INTO res_partner (company_id, name, email, street, city, zip) VALUES (1, ?, ?, ?, ?, ?)";
    private final String altaUsers = "INSERT INTO res_users (company_id, parthner_id, login, password, active, notification_type) VALUES (1, ?, ?, ?, ?, ?)";

    public DAO() {
        fichConf = ResourceBundle.getBundle("dataAccessTier.config");
        url = fichConf.getString("url");
        usuario = fichConf.getString("usuario");
        pass = fichConf.getString("pass");
    }

    // Para Abrir la Conexion
    public void openConnection() {
        try {
            con = DriverManager.getConnection(url, usuario, pass);
        } catch (SQLException e) {
            System.out.println("Error al intentar abrir la BD");
        }
    }

    // Para Cerrar la Conexion
    public void closeConnection() {

        try {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {

        }
    }

    @Override
    public void singUp(Usuario usuario) {

       

        // Se inserta la Primera Parte que corresponde ALTA_PARNER
        try {
            // Se abre conexion con postgres
            con = pool.getConnection();
            
            stmt = con.prepareStatement(altaParner);

            stmt.setString(2, usuario.getNombreyApellidos());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getDireccion());
            stmt.setString(5, usuario.getCiudad());
            stmt.setInt(6, usuario.getCodigoPostal());

            stmt.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Se inserta la Primera Parte que corresponde ALTA_USER
        try {
            stmt = con.prepareStatement(altaUsers);

            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getPassword());

            stmt.executeUpdate();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Se cierra la conexion con postgres
        closeConnection();
    }

}
