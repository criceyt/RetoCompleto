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
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import libreria.Mensaje;
import libreria.Request;

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

        contextMenu = new ContextMenu();
        MenuItem optionLight = new MenuItem("Tema Claro");
        MenuItem optionDark = new MenuItem("Tema Oscuro");
        MenuItem optionRetro = new MenuItem("Retro");
        contextMenu.getItems().addAll(optionLight, optionDark, optionRetro);

        loginPane.setOnMousePressed(this::showContextMenu);
        registerPane.setOnMousePressed(this::showContextMenu);

        optionLight.setOnAction(this::changeToLightTheme);
        optionDark.setOnAction(this::changeToDarkTheme);
        optionRetro.setOnAction(this::changeToRetroTheme);

        passwordFieldParent = (HBox) passwordField.getParent();
        registerPasswordFieldParent = (HBox) registerPasswordField.getParent();
        confirmPasswordFieldParent = (HBox) confirmPasswordField.getParent();

        revealButton.setOnAction(this::togglePasswordVisibility);
        revealRegisterButton.setOnAction(this::toggleRegisterPasswordVisibility);
        revealConfirmButton.setOnAction(this::toggleConfirmPasswordVisibility);
    }

    private void showContextMenu(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            contextMenu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }
    }

    private void changeToLightTheme(ActionEvent e) {

        changeToLightTheme();
    }

    private void changeToDarkTheme(ActionEvent e) {

        changeToDarkTheme();
    }

    private void changeToRetroTheme(ActionEvent e) {

        changeToRetroTheme();
    }

    private void togglePasswordVisibility(ActionEvent event) {
        if ("Mostrar".equals(revealButton.getText())) {
            passwordField.setVisible(false);
            plainTextField = new TextField(passwordField.getText());
            plainTextField.setVisible(true);
           
            passwordFieldParent.getChildren().set(0, plainTextField);
            revealButton.setText("Ocultar");

            plainTextField.textProperty().addListener((observable, oldValue, newValue) -> passwordField.setText(newValue));
        } else {
            passwordField.setText(plainTextField.getText());
            passwordField.setVisible(true);
            passwordFieldParent.getChildren().set(0, passwordField);
            revealButton.setText("Mostrar");
            plainTextField = null;
        }
    }

    private void toggleRegisterPasswordVisibility(ActionEvent event) {
        if ("Mostrar".equals(revealRegisterButton.getText())) {
            registerPasswordField.setVisible(false);
            plainRegisterTextField = new TextField(registerPasswordField.getText());
            plainRegisterTextField.setVisible(true);
            
            registerPasswordFieldParent.getChildren().set(0, plainRegisterTextField);
            revealRegisterButton.setText("Ocultar");

            plainRegisterTextField.textProperty().addListener((observable, oldValue, newValue) -> registerPasswordField.setText(newValue));
        } else {
            registerPasswordField.setText(plainRegisterTextField.getText());
            registerPasswordField.setVisible(true);
            registerPasswordFieldParent.getChildren().set(0, registerPasswordField);
            revealRegisterButton.setText("Mostrar");
            plainRegisterTextField = null;
        }
    }

    private void toggleConfirmPasswordVisibility(ActionEvent event) {
        if ("Mostrar".equals(revealConfirmButton.getText())) {
            confirmPasswordField.setVisible(false);
            plainConfirmTextField = new TextField(confirmPasswordField.getText());
            plainConfirmTextField.setVisible(true);
           
            confirmPasswordFieldParent.getChildren().set(0, plainConfirmTextField);
            revealConfirmButton.setText("Ocultar");

            plainConfirmTextField.textProperty().addListener((observable, oldValue, newValue) -> confirmPasswordField.setText(newValue));
        } else {
            confirmPasswordField.setText(plainConfirmTextField.getText());
            confirmPasswordField.setVisible(true);
            confirmPasswordFieldParent.getChildren().set(0, confirmPasswordField);
            revealConfirmButton.setText("Mostrar");
            plainConfirmTextField = null;
        }
    }

 

    private void changeToLightTheme() {

        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear(); // Limpiar estilos existentes
            scene.getStylesheets().add(getClass().getResource("/ui/stylesClaro.css").toExternalForm());
        } else {
            System.out.println("La escena es null");
        }
    }

    private void changeToDarkTheme() {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
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
        List<String> errores = new ArrayList<>();

        if (email.isEmpty() && password.isEmpty()) {
            errorHandler.handleGeneralException(new Exception("No hay ningún campo rellenado."), messageLabel);
            return;
        }

        if (password.isEmpty()) {
            errores.add("La contraseña no puede estar vacío.");
        }

        if (email.isEmpty()) {
            errores.add("El email no puede estar vacío.");
        }

        if (!esCorreoValido(email)) {
            errores.add("El correo electrónico no tiene un formato válido.");
        }

        if (!errores.isEmpty()) {
            String mensajeErrores = String.join("\n", errores);
            errorHandler.handleGeneralException(new Exception(mensajeErrores), messageLabel);
            return;
        }

        try {

            if (errorHandler.autenticar(email, password)) {
                Usuario usuario = new Usuario(email, password);
                Mensaje mensaje = new Mensaje(usuario, Request.SIGN_IN_REQUEST);
                Signable a = ClientFactory.getSignable();
                a.signIn(mensaje);

            }
        } catch (Exception e) {
            errorHandler.handleGeneralException(e, messageLabel);
        }
    }

    @FXML
    private void handleRegister() {
        List<String> errores = new ArrayList<>();
        if (plainRegisterTextField != null) {
            registerPasswordField.setText(plainRegisterTextField.getText());
        }
        if (plainConfirmTextField != null) {
            confirmPasswordField.setText(plainConfirmTextField.getText());
        }

        String nombreyApellidos = nombreyApellidoField.getText();
        String direccion = direccionField.getText();
        String ciudad = ciudadField.getText();
        String codigoPostalTexto = codigoPostalField.getText().trim();
        String email = emailField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        boolean estaActivo = activoCheckBox.isSelected();

        if (nombreyApellidos.isEmpty() && direccion.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty() && ciudad.isEmpty() && codigoPostalTexto.isEmpty()) {
            errorHandler.handleGeneralException(new Exception("No hay ningún campo rellenado."), messageLabel);
            return;
        }

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
            return; // Salimos del método si hay errores
        }

        try {
            int codigoPostal = Integer.parseInt(codigoPostalTexto);
            errorHandler.validarYRegistrar(nombreyApellidos, ciudad, codigoPostal, direccion, email, password, confirmPassword, estaActivo);

            Usuario usuario = new Usuario(email, password, nombreyApellidos, direccion, ciudad, codigoPostal, estaActivo);
            Mensaje mensaje = new Mensaje(usuario, Request.SIGN_UP_REQUEST);
            Signable a = ClientFactory.getSignable();
            a.singUp(mensaje);
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
        plainConfirmTextField.clear();
        plainRegisterTextField.clear();
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
