package dataAccessTier;

import java.sql.*;
import libreria.Signable;
import libreria.Usuario;

public class Dao implements Signable {

    private Connection connection;

    public Dao(Connection connection) {
        this.connection = connection; // Usa la conexión proporcionada
    }

    // Implementación del método para iniciar sesión
    @Override
    public boolean login(String username, String password) {
        String sql = "SELECT password FROM res_users WHERE login = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password); // Verifica la contraseña sin encriptar
            }
            return false; // Usuario no encontrado
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error según sea necesario
            return false;
        }
    }

    // Método para registrar un nuevo usuario
    @Override
    public boolean singUp(Usuario usuario) {
        String sql = "INSERT INTO res_users (login, password, name, street, city, zip, company_id, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getEmail()); // Usamos el email como nombre de usuario
            pstmt.setString(2, usuario.getPassword()); // Almacena la contraseña sin encriptar
            pstmt.setString(3, usuario.getName()); // Nombre y apellidos
            pstmt.setString(4, usuario.getDireccion()); // Dirección
            pstmt.setString(5, usuario.getCiudad()); // Ciudad
            pstmt.setString(6, usuario.getCodigoPostal()); // Código postal
            pstmt.setInt(7, usuario.getCompanyId()); // Establece el company_id
            pstmt.setBoolean(8, usuario.isActive()); // Establece el estado activo
            pstmt.executeUpdate();
            return true; // Registro exitoso
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error según sea necesario
            return false; // Si hay un error (como un usuario ya existente), devuelve false
        }
    }

}
