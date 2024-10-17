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
        // Validación: Campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            throw new Exception("Por favor, completa todos los campos.");
        }

        // Validación: Formato de correo
        if (!esCorreoValido(email)) {
            throw new Exception("El correo electrónico no tiene un formato válido.");
        }

        // Validación: Usuario no encontrado
        if (!usuariosRegistrados.containsKey(email)) {
            throw new Exception("Usuario no encontrado.");
        }

        Usuario usuario = usuariosRegistrados.get(email);

        // Validación: Contraseña incorrecta
        if (!usuario.getPassword().equals(password)) {
            throw new Exception("Contraseña incorrecta.");
        }

        return true;
    }

    public void validarYRegistrar(String nombreyApellidos, String ciudad, int codigoPostal, String direccion, String email, String password, String confirmPassword) throws Exception {
        // Validación: Campos vacíos
        if (nombreyApellidos.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || direccion.isEmpty() || ciudad.isEmpty() || codigoPostal == ' ') {
            throw new Exception("Por favor, completa todos los campos.");
        }

        // Validación: Formato de correo
        if (!esCorreoValido(email)) {
            throw new Exception("El correo electrónico no tiene un formato válido.");
        }

        // Validación: Usuario ya existente
        if (usuariosRegistrados.containsKey(email)) {
            throw new Exception("El usuario ya existe.");
        }

        // Validación: Contraseñas coinciden
        if (!password.equals(confirmPassword)) {
            throw new Exception("Las contraseñas no coinciden.");
        }

        // Validación: Fortaleza de la contraseña
        if (!esContraseñaFuerte(password)) {
            throw new Exception("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula, y un número.");
        }

        // Registro exitoso del nuevo usuario
        Usuario nuevoUsuario = new Usuario(email, password, nombreyApellidos, direccion, ciudad, codigoPostal);
        usuariosRegistrados.put(email, nuevoUsuario);
    }

    /**
     * Manejo de excepciones generales.
     */
    public static void handleGeneralException(Exception e, Label messageLabel) {
        logger.log(Level.SEVERE, "Ha ocurrido un error: " + e.getMessage(), e);
        showAlert("Error", "Ha ocurrido un error inesperado", e.getMessage(), AlertType.ERROR);
        if (messageLabel != null) {
            messageLabel.setText(e.getMessage()); // Muestra el mensaje en el Label
        }
    }

    /**
     * Método auxiliar para mostrar una alerta al usuario.
     */
       private static void showAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait(); 
    }

    /**
     * Valida el formato del correo electrónico. Utiliza una expresión regular
     * para comprobar si el correo es válido.
     */
    private boolean esCorreoValido(String email) {
        // Expresión regular para validar un correo electrónico
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
    }

    /**
     * Valida la fortaleza de la contraseña. Debe tener al menos 8 caracteres,
     * incluyendo letras mayúsculas, minúsculas y números.
     */
    private boolean esContraseñaFuerte(String password) {
        // Expresión regular para validar una contraseña fuerte
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(passwordRegex);
    }

    /**
     * Método para validar y registrar un nuevo usuario. Verifica todos los
     * campos, formato del correo, fortaleza de la contraseña, y si el usuario
     * ya existe.
     */
}
