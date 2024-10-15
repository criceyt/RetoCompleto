package userInterfaceTier;

import libreria.Signable;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import validations.ErrorHandler;
import libreria.Usuario;
import libreria.Message;

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
            // Crear objeto Usuario y Message para el login
            Usuario usuario = new Usuario(email, password, null, null, null, null);
            Message mensaje = new Message(usuario);

            // Invocar el método login
            Signable signable = ClientFactory.getSignable();
            boolean loginExitoso = signable.login(mensaje);

            if (loginExitoso) {
                messageLabel.setText("¡Inicio de sesión exitoso!");
                System.out.println("Usuario autenticado: " + email);
            } else {
                showAlert("Error en el inicio de sesión", "Credenciales incorrectas.");
            }
        } catch (Exception e) {
            errorHandler.handleGeneralException(e, messageLabel);
        }
    }

    @FXML
    private void handleRegister() {
        // Obtener los datos del formulario
        String nombreyApellidos = nombreyApellidoField.getText().trim();
        String direccion = direccionField.getText().trim();
        String ciudad = ciudadField.getText().trim();
        String codigoPostalTexto = codigoPostalField.getText().trim();
        String email = emailField.getText().trim();
        String password = registerPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Validar campos requeridos
        List<String> errores = new ArrayList<>();
        if (nombreyApellidos.isEmpty() || direccion.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty() || ciudad.isEmpty()
                || codigoPostalTexto.isEmpty()) {
            errores.add("Por favor, completa todos los campos.");
        }

        // Validar email
        if (!esCorreoValido(email)) {
            errores.add("El correo electrónico no tiene un formato válido.");
        }

        // Comparar contraseñas
        if (!password.equals(confirmPassword)) {
            errores.add("Las contraseñas no coinciden.");
        }

        // Verificar fuerza de la contraseña
        if (!esContraseñaFuerte(password)) {
            errores.add("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula y un número.");
        }

        // Validar código postal
        if (!codigoPostalTexto.matches("\\d{5}")) {
            errores.add("El código postal debe tener exactamente 5 números.");
        }

        // Mostrar errores si existen
        if (!errores.isEmpty()) {
            String mensajeErrores = String.join("\n", errores);
            errorHandler.handleGeneralException(new Exception(mensajeErrores), messageLabel);
            return; // Salir del método si hay errores
        }

        // Crear el objeto Usuario
        Usuario usuario = new Usuario(email, password, nombreyApellidos, direccion, ciudad, codigoPostalTexto);

        // Crear el objeto Message con el Usuario
        Message mensaje = new Message("register", usuario); // Asegúrate de que el constructor de Message acepte la acción
        System.out.println("Mensaje creado: " + mensaje); // Mensaje de depuración

        // Enviar el mensaje al servidor
        try {
            Signable signable = ClientFactory.getSignable(); // Obtener la instancia del cliente
            boolean registrado = signable.singUp(mensaje); // Llamar al método de registro

            // Mostrar resultado de la operación
            if (registrado) {
                showAlert("Registro exitoso", "El usuario ha sido registrado correctamente.");
                messageLabel.setText("¡Registro exitoso! Ahora puedes iniciar sesión.");
                limpiarCamposRegistro();
            } else {
                showAlert("Error en el registro", "No se pudo registrar al usuario. Puede que ya exista.");
            }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION); // Cambiar a AlertType.ERROR si es necesario
        alert.setTitle(title);
        alert.setHeaderText(null); // Puedes establecer un encabezado si lo deseas
        alert.setContentText(message);
        alert.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre
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
