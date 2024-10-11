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
import libreria.Usuario;

import java.util.ArrayList;
import java.util.List;

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
    private TextField nombreyApellidoField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private TextField ciudadField;

    @FXML
    private TextField codigoPostalField;

    @FXML
    private TextField direccionField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField confirmPasswordField;

    private ErrorHandler errorHandler = new ErrorHandler();

    public SignController() {
    }

    @FXML
    private void handleLogin() {
        String email = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (errorHandler.autenticar(email, password)) {
                messageLabel.setText("¡Inicio de sesión exitoso!");
                System.out.println("Usuario autenticado: " + email);
            }
        } catch (Exception e) {
            errorHandler.handleGeneralException(e, messageLabel);
        }
    }

    @FXML
    private void handleRegister() {
        String nombreyApellidos = nombreyApellidoField.getText();
        String direccion = direccionField.getText();
        String ciudad = ciudadField.getText();
        String codigoPostalTexto = codigoPostalField.getText().trim();
        String email = emailField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        List<String> errores = new ArrayList<>();

        if (nombreyApellidos.isEmpty() || direccion.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || ciudad.isEmpty() || codigoPostalTexto.isEmpty()) {
            errorHandler.handleGeneralException(new Exception("Por favor, completa todos los campos."), messageLabel);
            return;
        }

        if (!esCorreoValido(email)) {
            errores.add("El correo electrónico no tiene un formato válido.");
        }

        if (!password.equals(confirmPassword)) {
            errores.add("Las contraseñas no coinciden.");
        }

        if (!esContraseñaFuerte(password)) {
            errores.add("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula y un número.");
        }

        if (!codigoPostalTexto.matches("\\d{5}")) {
            errores.add("El código postal debe tener exactamente 5 números.");
        }

        if (!errores.isEmpty()) {
            String mensajeErrores = String.join("\n", errores);
            errorHandler.handleGeneralException(new Exception(mensajeErrores), messageLabel);
            return;
        }

        try {
            errorHandler.validarYRegistrar(nombreyApellidos, ciudad, codigoPostalTexto, direccion, email, password, confirmPassword);
            messageLabel.setText("¡Registro exitoso! Ahora puedes iniciar sesión.");
           Usuario usuario = new Usuario(email, password, nombreyApellidos, direccion, ciudad, codigoPostalTexto);

            Signable signable = ClientFactory.getSignable();
            signable.singUp(usuario);
            limpiarCamposRegistro();
        } catch (Exception e) {
            errorHandler.handleGeneralException(e, messageLabel);
        }
    }

    private void limpiarCamposRegistro() {
        nombreyApellidoField.clear();
        ciudadField.clear();
        codigoPostalField.clear();
        direccionField.clear();
        emailField.clear();
        registerPasswordField.clear();
        confirmPasswordField.clear();
    }

    private boolean esCorreoValido(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean esContraseñaFuerte(String password) {
        return password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*");
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
