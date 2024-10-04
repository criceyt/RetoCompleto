package persistence;

import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsuarioDAO {
    private Connection connection;

    // Constructor que recibe una conexión
    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void guardar(Usuario usuario) throws SQLException {
        String query = "INSERT INTO res_users (nombre, apellido, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getPassword());
            stmt.executeUpdate();
        }
    }

    public boolean existeUsuario(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM res_users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0; // Devuelve true si existe
        }
    }

    public Optional<Usuario> obtenerPorEmail(String email) throws SQLException {
        String query = "SELECT * FROM res_users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Usuario(
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("password")
                ));
            }
        }
        return Optional.empty();
    }

    public Usuario obtenerPorUsername(String email) throws SQLException {
        String query = "SELECT * FROM res_users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        }
        return null; // Usuario no encontrado
    }
}
