package businesslogic;

import java.sql.SQLException;
import model.Usuario;
import persistence.UsuarioDAO;
import java.util.Optional;

public class UsuarioService {
    private UsuarioDAO usuarioDAO; // Dependencia de UsuarioDAO

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void registrar(Usuario usuario) throws Exception {
        // Verificar si el usuario ya existe
        if (usuarioDAO.existeUsuario(usuario.getEmail())) {
            throw new Exception("El usuario ya existe.");
        }
        // Guardar el nuevo usuario en la base de datos
        usuarioDAO.guardar(usuario);
    }

    public boolean autenticar(String email, String password) throws Exception {
        // Comprobar las credenciales del usuario
        Optional<Usuario> usuarioOpt = usuarioDAO.obtenerPorEmail(email);
        
        // Comprobar si el usuario existe y la contraseña es correcta
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password)) {
                return true; // Autenticación exitosa
            } else {
                throw new Exception("Contraseña incorrecta."); // Contraseña no coincide
            }
        } else {
            throw new Exception("El usuario no existe."); // Usuario no encontrado
        }
    }

    public Usuario obtenerPorUsername(String email) throws SQLException {
        return usuarioDAO.obtenerPorUsername(email);
    }
}
