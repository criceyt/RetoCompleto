
import java.sql.*;
import libreria.Signable;
import libreria.Usuario;

public class Dao implements Signable {

    private String url;
    private String user;
    private String password;

    public Dao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    // Método para establecer la conexión a la base de datos
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Implementación del método para iniciar sesión
    @Override
    public boolean login(String username, String password) {
        String sql = "SELECT password FROM res_users WHERE login = ?";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    @Override
    public void singUp(Usuario usuario) {
        String sql = "INSERT INTO res_users (login, password, name, street, city, zip) VALUES (?, ?, ?, ?, ?, ?)"; // Nombres de columnas en inglés
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getEmail()); // Usamos el email como nombre de usuario
            pstmt.setString(2, usuario.getPassword()); // Almacena la contraseña sin encriptar
            pstmt.setString(3, usuario.getNombreyApellidos()); // Nombre y apellidos
            pstmt.setString(4, usuario.getDireccion()); // Dirección
            pstmt.setString(5, usuario.getCiudad()); // Ciudad
            pstmt.setString(6, usuario.getCodigoPostal()); // Código postal
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error según sea necesario
        }
    }

}
