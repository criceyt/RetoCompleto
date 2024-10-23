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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;

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
    private Button revealButton;

    @FXML
    private Button revealRegisterButton;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Scene scene;
    @FXML
    private Button revealConfirmButton;
    @FXML
    private TextField plainTextField;
    @FXML
    private TextField plainRegisterTextField;
    @FXML
    private TextField plainConfirmTextField;
    @FXML
    private HBox passwordFieldParent;
    @FXML
    private HBox registerPasswordFieldParent;
    @FXML
    private HBox confirmPasswordFieldParent;
    @FXML
    private CheckBox activoCheckBox;

    private ContextMenu contextMenu;
    private boolean isDarkTheme = true;
    // Dependencia al ErrorHandler
    private ErrorHandler errorHandler = new ErrorHandler();

    // Constructor sin parámetros
    public SignController() {
    }

    @FXML
    public void initialize() {

        // Crear el menú contextual
        contextMenu = new ContextMenu();
        MenuItem optionLight = new MenuItem("Tema Claro");
        MenuItem optionDark = new MenuItem("Tema Oscuro");
        MenuItem optionRetro = new MenuItem("Retro");
        contextMenu.getItems().addAll(optionLight, optionDark, optionRetro);

        loginPane.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(loginPane, event.getScreenX(), event.getScreenY());
            }
        });

        registerPane.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(registerPane, event.getScreenX(), event.getScreenY());
            }
        });

        // Vincular acciones de los MenuItems
        optionLight.setOnAction(e -> changeToLightTheme());
        optionDark.setOnAction(e -> changeToDarkTheme());
        optionRetro.setOnAction(e -> changeToRetroTheme());

        passwordFieldParent = (HBox) passwordField.getParent();
        registerPasswordFieldParent = (HBox) registerPasswordField.getParent();
        confirmPasswordFieldParent = (HBox) confirmPasswordField.getParent();

        revealButton.setOnAction(event -> {
            if (revealButton.getText().equals("Mostrar")) {
                passwordField.setVisible(false);
                plainTextField = new TextField(passwordField.getText());
                plainTextField.setVisible(true);
                plainTextField.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-border-color: #888; -fx-border-radius: 10; -fx-pref-width: 200; -fx-min-width: 200; -fx-max-width: 300;");
                passwordFieldParent.getChildren().set(0, plainTextField);
                revealButton.setText("Ocultar");
            } else {
                if (plainTextField != null) {
                    passwordField.setText(plainTextField.getText());
                    passwordField.setVisible(true);
                    passwordFieldParent.getChildren().set(0, passwordField);
                    revealButton.setText("Mostrar");
                    plainTextField = null;
                }
            }
        });

        revealRegisterButton.setOnAction(event -> {
            if (revealRegisterButton.getText().equals("Mostrar")) {
                registerPasswordField.setVisible(false);
                plainRegisterTextField = new TextField(registerPasswordField.getText());
                plainRegisterTextField.setVisible(true);
                plainRegisterTextField.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-border-color: #888; -fx-border-radius: 10; -fx-pref-width: 200; -fx-min-width: 200; -fx-max-width: 300;");
                registerPasswordFieldParent.getChildren().set(0, plainRegisterTextField);
                revealRegisterButton.setText("Ocultar");
            } else {
                if (plainRegisterTextField != null) {
                    registerPasswordField.setText(plainRegisterTextField.getText());
                    registerPasswordField.setVisible(true);
                    registerPasswordFieldParent.getChildren().set(0, registerPasswordField);
                    revealRegisterButton.setText("Mostrar");
                    plainRegisterTextField = null;
                }
            }
        });

        revealButton.setOnAction(event -> {
            if (revealButton.getText().equals("Mostrar")) {
                passwordField.setVisible(false);
                plainTextField = new TextField(passwordField.getText());
                plainTextField.setVisible(true);
                plainTextField.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-border-color: #888; -fx-border-radius: 10; -fx-pref-width: 200; -fx-min-width: 200; -fx-max-width: 300;");
                passwordFieldParent.getChildren().set(0, plainTextField);
                revealButton.setText("Ocultar");

                // Listener para actualizar el PasswordField en tiempo real
                plainTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    passwordField.setText(newValue);
                });
            } else {
                if (plainTextField != null) {
                    passwordField.setText(plainTextField.getText());
                    passwordField.setVisible(true);
                    passwordFieldParent.getChildren().set(0, passwordField);
                    revealButton.setText("Mostrar");
                    plainTextField = null;
                }
            }
        });

        revealConfirmButton.setOnAction(event -> {
            if (revealConfirmButton.getText().equals("Mostrar")) {
                confirmPasswordField.setVisible(false);
                plainConfirmTextField = new TextField(confirmPasswordField.getText());
                plainConfirmTextField.setVisible(true);
                plainConfirmTextField.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-border-color: #888; -fx-border-radius: 10; -fx-pref-width: 200; -fx-min-width: 200; -fx-max-width: 300;");
                confirmPasswordFieldParent.getChildren().set(0, plainConfirmTextField);
                revealConfirmButton.setText("Ocultar");

                // Listener para actualizar el PasswordField en tiempo real
                plainConfirmTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    confirmPasswordField.setText(newValue);
                });
            } else {
                if (plainConfirmTextField != null) {
                    confirmPasswordField.setText(plainConfirmTextField.getText());
                    confirmPasswordField.setVisible(true);
                    confirmPasswordFieldParent.getChildren().set(0, confirmPasswordField);
                    revealConfirmButton.setText("Mostrar");
                    plainConfirmTextField = null;
                }
            }
        });

    }

    private void changeToLightTheme() {
        // Asegúrate de que loginPane tiene una escena antes de intentar acceder
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear(); // Limpiar estilos existentes
            scene.getStylesheets().add(getClass().getResource("/ui/stylesClaro.css").toExternalForm());
        } else {
            System.out.println("La escena es null");
        }
    }

    // Método para cambiar al tema oscuro
    private void changeToDarkTheme() {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear(); // Limpiar estilos existentes
            scene.getStylesheets().add(getClass().getResource("/ui/stylesOscuro.css").toExternalForm());
        } else {
            System.out.println("La escena es null");
        }
    }

    private void changeToRetroTheme() {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear(); // Limpiar estilos existentes
            scene.getStylesheets().add(getClass().getResource("/ui/stylesRetro.css").toExternalForm());
        } else {
            System.out.println("La escena es null");
        }
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
        boolean estaActivo = activoCheckBox.isSelected();

        List<String> errores = new ArrayList<>();
        if (plainRegisterTextField != null) {
            registerPasswordField.setText(plainRegisterTextField.getText());
        }
        if (plainConfirmTextField != null) {
            confirmPasswordField.setText(plainConfirmTextField.getText());
        }
        // Verificar si todos los campos están vacíos
        if (nombreyApellidos.isEmpty() && direccion.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty() && ciudad.isEmpty() && codigoPostalTexto.isEmpty()) {
            errorHandler.handleGeneralException(new Exception("No hay ningún campo rellenado."), messageLabel);
            return;
        }

        // Validar campos vacíos
        if (nombreyApellidos.isEmpty()) {
            errores.add("El nombre completo no puede estar vacío.");
        }
        if (codigoPostalTexto.isEmpty()) {
            errores.add("El código postal no puede estar vacío.");
        }
        if (ciudad.isEmpty()) {
            errores.add("La ciudad no puede estar vacía.");
        }
        if (direccion.isEmpty()) {
            errores.add("La dirección no puede estar vacía.");
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
            errores.add("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula y un número.");
        }

        // Validar código postal
        if (!codigoPostalTexto.matches("\\d{5}")) { // Verifica que sean 5 dígitos
            errores.add("El código postal debe tener exactamente 5 números.");
        }

        // Si hay errores, mostrar la alerta con todos los errores
        if (!errores.isEmpty()) {
            String mensajeErrores = String.join("\n", errores);
            errorHandler.handleGeneralException(new Exception(mensajeErrores), messageLabel);
            return; // Salimos del método si hay errores
        }

        // Registrar nuevo usuario usando el ErrorHandler
        try {
            int codigoPostal = Integer.parseInt(codigoPostalTexto);
            errorHandler.validarYRegistrar(nombreyApellidos, ciudad, codigoPostal, direccion, email, password, confirmPassword, estaActivo);
            
            //messageLabel.setText("¡Registro exitoso! Ahora puedes iniciar sesión.");
            Usuario usuario = new Usuario(email, password, nombreyApellidos, direccion, ciudad, codigoPostal, estaActivo);
            Signable a = ClientFactory.getSignable();
            a.singUp(usuario);
            limpiarCamposRegistro();
        } catch (Exception e) {
            errorHandler.handleGeneralException(e, messageLabel); // Maneja todos los errores
        }
    }

    // Método para limpiar los campos después del registro
    private void limpiarCamposRegistro() {
        nombreyApellidoField.clear();
        ciudadField.clear();
        codigoPostalField.clear();
        direccionField.clear();
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
