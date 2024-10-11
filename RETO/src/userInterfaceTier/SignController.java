package userInterfaceTier;

import libreria.Signable;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import validations.ErrorHandler; 

import java.util.ArrayList;
import java.util.List;
import libreria.Usuario;

public class SignController {

    @FXML
    private VBox loginPane;

    @FXML
    private VBox registerPane;

    @FXML
    private Label messageLabel;

    @FXML
    private VBox messagePane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField registerUsernameField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField confirmPasswordField;

    // Dependencia al ErrorHandler
    private ErrorHandler errorHandler = new ErrorHandler();

    // Constructor sin parámetros
    public SignController() {
  
    }

    @FXML
    private void handleLogin() {
        String email = usernameField.getText();
        String password = passwordField.getText();

        try {
            // Autenticar usuario
            if (errorHandler.autenticar(email, password)) {
                messageLabel.setText("¡Inicio de sesión exitoso!");
                System.out.println("Usuario autenticado: " + email);
            }
        } catch (Exception e) {
            errorHandler.handleGeneralException(e); 
        }
    }

    @FXML
    private void handleRegister() {
        String nombre = firstNameField.getText();
        String apellido = lastNameField.getText();
        String email = emailField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        List<String> errores = new ArrayList<>();

        // Verificar si todos los campos están vacíos
        if (nombre.isEmpty() && apellido.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
            errorHandler.handleGeneralException(new Exception("No hay ningún campo rellenado."));
            return; 
        }

        // Validar campos vacíos
        if (nombre.isEmpty()) {
            errores.add("El nombre no puede estar vacío.");
        }
        if (apellido.isEmpty()) {
            errores.add("El apellido no puede estar vacío.");
        }
        if (email.isEmpty()) {
            errores.add("El correo electrónico no puede estar vacío.");
        }
        if (password.isEmpty()) {
            errores.add("La contraseña no puede estar vacía.");
        }
        if (confirmPassword.isEmpty()) {
            errores.add("Confirma tu contraseña.");
        }

        // Validar formato de correo
        if (!esCorreoValido(email)) {
            errores.add("El correo electrónico no tiene un formato válido.");
        }

        // Validar contraseñas coincidentes
        if (!password.equals(confirmPassword)) {
            errores.add("Las contraseñas no coinciden.");
        }

        // Validar fortaleza de la contraseña
        if (!esContraseñaFuerte(password)) {
            errores.add("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula, y un número.");
        }

        // Si hay errores, mostrar la alerta con todos los errores
        if (!errores.isEmpty()) {
            String mensajeErrores = String.join("\n", errores);
            errorHandler.handleGeneralException(new Exception(mensajeErrores));
            return; // Salimos del método si hay errores
        }

        // Registrar nuevo usuario usando el ErrorHandler
        try {
            errorHandler.validarYRegistrar(nombre, apellido, email, password, confirmPassword);
            messageLabel.setText("¡Registro exitoso! Ahora puedes iniciar sesión.");
            Usuario usuario = new Usuario(nombre, apellido, email, password);
            Signable a = ClientFactory.getSignable();
            a.singUp(usuario);
            limpiarCamposRegistro();
        } catch (Exception e) {
            errorHandler.handleGeneralException(e); // Maneja todos los errores
        }
    }

    // Método para limpiar los campos después del registro
    private void limpiarCamposRegistro() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        registerPasswordField.clear();
        confirmPasswordField.clear();
    }

    @FXML
    private void showRegister() {
        transitionToRegister();
    }

    @FXML
    private void showLogin() {
        transitionToLogin();
    }

    private void transitionToRegister() {
        loginPane.setVisible(false);
        registerPane.setVisible(true);
        registerPane.setTranslateX(loginPane.getWidth());

        TranslateTransition transitionIn = new TranslateTransition(Duration.millis(300), registerPane);
        transitionIn.setToX(0);
        transitionIn.play();

        TranslateTransition messageTransition = new TranslateTransition(Duration.millis(300), messagePane);
        messageTransition.setFromX(0);
        messageTransition.setToX(-loginPane.getWidth());
        messageTransition.play();
    }

    private void transitionToLogin() {
        registerPane.setVisible(false);
        loginPane.setVisible(true);
        loginPane.setTranslateX(-loginPane.getWidth());

        TranslateTransition transitionIn = new TranslateTransition(Duration.millis(300), loginPane);
        transitionIn.setToX(0);
        transitionIn.play();

        TranslateTransition messageTransition = new TranslateTransition(Duration.millis(300), messagePane);
        messageTransition.setFromX(0);
        messageTransition.setToX(loginPane.getWidth());
        messageTransition.play();
    }

    // Método para validar el formato del correo (simple)
    private boolean esCorreoValido(String email) {
        return email.contains("@") && email.contains(".");
    }

    // Método para validar la fortaleza de la contraseña (ejemplo básico)
    private boolean esContraseñaFuerte(String password) {
        return password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*");
    }
}
