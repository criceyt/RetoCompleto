package dataAccessTier;

import java.sql.*;
import libreria.Message;
import libreria.Signable;
import libreria.Usuario;

public class Dao implements Signable {

    private Connection connection;

    public Dao(Connection connection) {
        this.connection = connection; // Usa la conexión proporcionada
    }

    // Implementación del método para iniciar sesión
    @Override
    public boolean login(Message mensaje) {
        Usuario usuario = mensaje.getUsuario(); // Obtener el usuario del objeto Message
        String sql = "SELECT password FROM res_users WHERE login = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getEmail());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(usuario.getPassword()); // Verifica la contraseña sin encriptar
            }
            return false; // Usuario no encontrado o contraseña incorrecta
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error según sea necesario
            return false;
        }
    }

    // Método para registrar un nuevo usuario
    @Override
    public boolean singUp(Message mensaje) {
        Usuario usuario = mensaje.getUsuario(); // Obtener el usuario del objeto Message
        String sqlPartner = "INSERT INTO res_partner (name, company_id, street, city, zip) VALUES (?, ?, ?, ?, ?) RETURNING id";
        String sqlUser = "INSERT INTO res_users (login, password, partner_id, company_id, active) VALUES (?, ?, ?, ?, true)";

        try {
            connection.setAutoCommit(false); // Iniciar transacción

            // Insertar en res_partner
            int partnerId;
            try (PreparedStatement pstmtPartner = connection.prepareStatement(sqlPartner)) {
                pstmtPartner.setString(1, usuario.getNombreyApellidos()); // Nombre y apellidos
                pstmtPartner.setInt(2, 1); // company_id es 1 por defecto
                pstmtPartner.setString(3, usuario.getDireccion()); // Dirección
                pstmtPartner.setString(4, usuario.getCiudad()); // Ciudad
                pstmtPartner.setString(5, usuario.getCodigoPostal()); // Código postal

                ResultSet rs = pstmtPartner.executeQuery();
                if (rs.next()) {
                    partnerId = rs.getInt(1); // Obtener el ID del socio creado
                } else {
                    throw new SQLException("Error al insertar en res_partner.");
                }
            }

            // Insertar en res_users
            try (PreparedStatement pstmtUser = connection.prepareStatement(sqlUser)) {
                pstmtUser.setString(1, usuario.getEmail()); // Email como login
                pstmtUser.setString(2, usuario.getPassword()); // Contraseña
                pstmtUser.setInt(3, partnerId); // Asociar el partner_id recién creado
                pstmtUser.setInt(4, 1); // company_id es 1 por defecto

                pstmtUser.executeUpdate();
            }

            connection.commit(); // Confirmar la transacción
            return true; // Registro exitoso
        } catch (SQLException e) {
            try {
                connection.rollback(); // Deshacer los cambios si algo falla
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace(); // Manejar el error
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Restaurar el auto-commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
