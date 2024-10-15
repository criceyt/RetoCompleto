package dataAccessTier;

import libreria.Message;
import libreria.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import libreria.Signable;

public class Dao implements Signable {

    private Connection connection;

    public Dao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean login(Message mensaje) {
        String sql = "SELECT password FROM res_users WHERE login = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mensaje.getUsuario().getEmail());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // Comparar la contraseña tal como está (sin cifrar)
                return storedPassword.equals(mensaje.getUsuario().getPassword());
            }
            return false; // Usuario no encontrado
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean singUp(Message mensaje) {
        Usuario usuario = mensaje.getUsuario();
        try {
            connection.setAutoCommit(false); // Iniciar la transacción

            // Verificar si el usuario ya existe
            String checkUserSql = "SELECT id FROM res_users WHERE login = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, usuario.getEmail());
                ResultSet checkRs = checkStmt.executeQuery();
                if (checkRs.next()) {
                    System.out.println("Usuario ya existe.");
                    return false; // Usuario ya existe, no continuar con el registro
                }
            }

            // Insertar en res_partner
            String partnerSql = "INSERT INTO res_partner (name, street, city, zip, company_id) VALUES (?, ?, ?, ?, ?) RETURNING id";
            try (PreparedStatement pstmtPartner = connection.prepareStatement(partnerSql)) {
                pstmtPartner.setString(1, usuario.getNombreyApellidos());
                pstmtPartner.setString(2, usuario.getDireccion());
                pstmtPartner.setString(3, usuario.getCiudad());
                pstmtPartner.setString(4, usuario.getCodigoPostal());
                pstmtPartner.setInt(5, 1); // company_id
                ResultSet partnerRs = pstmtPartner.executeQuery();

                if (partnerRs.next()) {
                    int partnerId = partnerRs.getInt(1);

                    // Insertar en res_users
                    String userSql = "INSERT INTO res_users (login, password, partner_id, active) VALUES (?, ?, ?, true)";
                    try (PreparedStatement pstmtUser = connection.prepareStatement(userSql)) {
                        pstmtUser.setString(1, usuario.getEmail());
                        pstmtUser.setString(2, usuario.getPassword()); // Guardar la contraseña sin cifrar
                        pstmtUser.setInt(3, partnerId);
                        pstmtUser.executeUpdate();
                    }
                } else {
                    throw new SQLException("Error al crear el socio.");
                }
            }

            connection.commit();
            return true; // Registro exitoso
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Deshacer cambios en caso de error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace(); // Manejar rollback
                }
            }
            e.printStackTrace();
            return false; // Indica que el registro falló
        }
    }
}
