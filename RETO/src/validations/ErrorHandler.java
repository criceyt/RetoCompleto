package validations;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Label;
import libreria.Usuario;

public class ErrorHandler {

    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());
    private Map<String, Usuario> usuariosRegistrados = new HashMap<>();

    /**
     * Método para autenticar a un usuario durante el inicio de sesión. Verifica
     * si el usuario existe y si la contraseña es correcta.
     */
    public boolean autenticar(String email, String password) throws Exception {
        if (email.isEmpty() || password.isEmpty()) {
            throw new Exception("Por favor, completa todos los campos.");
        }

        if (!esCorreoValido(email)) {
            throw new Exception("El correo electrónico no tiene un formato válido.");
        }

        if (!usuariosRegistrados.containsKey(email)) {
            throw new Exception("Usuario no encontrado.");
        }

        Usuario usuario = usuariosRegistrados.get(email);

        if (!usuario.getPassword().equals(password)) {
            throw new Exception("Contraseña incorrecta.");
        }

        return true;
    }

    public void validarYRegistrar(String nombreyApellidos, String ciudad, String codigoPostal, String direccion, String email, String password, String confirmPassword) throws Exception {
        // Validar si todos los campos están vacíos
        if (nombreyApellidos.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || direccion.isEmpty() || ciudad.isEmpty() || codigoPostal.isEmpty()) {
            throw new Exception("Por favor, completa todos los campos.");
        }

        // Validar formato de correo
        if (!esCorreoValido(email)) {
            throw new Exception("El correo electrónico no tiene un formato válido.");
        }

        // Validar si el usuario ya está registrado
        if (usuariosRegistrados.containsKey(email)) {
            throw new Exception("El usuario ya existe.");
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            throw new Exception("Las contraseñas no coinciden.");
        }

        // Validar fortaleza de la contraseña
        if (!esContraseñaFuerte(password)) {
            throw new Exception("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula y un número.");
        }

        // Validar que el código postal sea numérico
        if (!codigoPostal.matches("\\d+")) {
            throw new Exception("El código postal debe contener exactamente 5 números.");
        }

        // Registro exitoso
        Usuario nuevoUsuario = new Usuario(email, password, nombreyApellidos, direccion, ciudad, codigoPostal);
        usuariosRegistrados.put(email, nuevoUsuario);
    }

    public static void handleGeneralException(Exception e, Label messageLabel) {
        logger.log(Level.SEVERE, "Ha ocurrido un error: " + e.getMessage(), e);
        showAlert("Error", "Ha ocurrido un error inesperado", e.getMessage(), AlertType.ERROR);
        if (messageLabel != null) {
            messageLabel.setText(e.getMessage());
        }
    }

    private static void showAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean esCorreoValido(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
    }

    private boolean esContraseñaFuerte(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(passwordRegex);
    }
}
