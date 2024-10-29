package dataAccessTier;

import excepciones.ErrorGeneral;
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

public class DAO implements Signable {

    private PoolConexiones pool;

    // HERREMIENTAS
    private Connection con;
    private PreparedStatement stmt;

    private ResourceBundle fichConf;
    private String url;
    private String usuario;
    private String pass;

    // CONSULTA
    private final String altaParner = "INSERT INTO res_partner (company_id, name, email, street, city, zip) VALUES (1, ?, ?, ?, ?, ?)";
    private final String altaUsers = "INSERT INTO res_users (company_id, partner_id, login, password, active) VALUES (1, ?, ?, ?, ?)";
    private final String selectParnerId = "SELECT id FROM res_partner order by id desc limit 1";
    private final String inicioSesion = "SELECT company_id, partner_id, login, password, active FROM res_users WHERE login=? AND password=?";
    private final String comprobarEmailRegistrado = "SELECT COUNT(*) FROM res_users WHERE email = ?";

    public DAO() throws SQLException {
        this.pool = new PoolConexiones();
    }

    private void conexionRealizada() throws SQLException {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                pool.returnConnection(con);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Mensaje singUp(Mensaje mensaje) /*throws EmailRepetido*/{

        // Se inserta la Primera Parte que corresponde ALTA_PARTNER
        try {
            // Se abre conexion con postgres
            con = pool.getConnection();

            stmt = con.prepareStatement(comprobarEmailRegistrado);
            stmt.setString(1, mensaje.getUser().getEmail());
            ResultSet rs;
            rs = stmt.executeQuery();

            if (rs.next()) {
                //throw EmailRepetido();
            } else {
                stmt = con.prepareStatement(altaParner);

                stmt.setString(1, mensaje.getUser().getNombreyApellidos());
                stmt.setString(2, mensaje.getUser().getEmail());
                stmt.setString(3, mensaje.getUser().getDireccion());
                stmt.setString(4, mensaje.getUser().getCiudad());
                stmt.setInt(5, mensaje.getUser().getCodigoPostal());
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mensaje;

    }

    @Override
    public synchronized Mensaje signIn(Mensaje mensaje) throws ErrorGeneral{
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
                
                if(isActive){
                    mensaje.setRq(Request.SIGN_IN_EXITOSO);
                }else{
                    mensaje.setRq(Request.USUARIO_NO_ACTIVO);
                }
            } else {
                mensaje.setRq(Request.USUARIO_INEXISTENTE);
            }

            conexionRealizada();

        } catch (SQLException ex) {
            throw new ErrorGeneral();
        }
        return mensaje;
    }
}
