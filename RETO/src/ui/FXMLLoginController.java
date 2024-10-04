package ui;

import businesslogic.UsuarioService;
import model.Usuario;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class FXMLLoginController {

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

    private UsuarioService usuarioService;

    // Constructor sin parámetros
    public FXMLLoginController() {
        // Constructor vacío
    }

    // Método para establecer el servicio de usuario
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @FXML
    private void handleLogin() {
        String email = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (usuarioService.autenticar(email, password)) {
                messageLabel.setText("¡Inicio de sesión exitoso!");
                System.out.println("Usuario autenticado: " + email);
            } else {
                throw new Exception("Credenciales incorrectas. Intenta nuevamente.");
            }
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
            System.out.println("Error en autenticación: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        String nombre = "NombreEjemplo"; // Cambia esto por un campo de texto si lo necesitas
        String apellido = "ApellidoEjemplo"; // Cambia esto por un campo de texto si lo necesitas
        String email = registerUsernameField.getText(); // Ahora se usa como email
        String password = registerPasswordField.getText();

        try {
            // Validar que no haya campos vacíos
            if (email.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                throw new Exception("Por favor, completa todos los campos.");
            }

            // Crear el usuario y llamar al servicio de registro
            Usuario nuevoUsuario = new Usuario(nombre, apellido, email, password);
            usuarioService.registrar(nuevoUsuario);
            messageLabel.setText("¡Registro exitoso! Ahora puedes iniciar sesión.");
            System.out.println("Usuario registrado: " + email);

            // Limpia los campos después del registro
            registerUsernameField.clear();
            registerPasswordField.clear();
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
            System.out.println("Error en registro: " + e.getMessage());
        }
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
}
