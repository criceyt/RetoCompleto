package userInterfaceTier;

import libreria.Signable;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import validations.ErrorHandler;
import libreria.Usuario;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    private ContextMenu contextMenu;
    private boolean isDarkTheme = true;
    private ErrorHandler errorHandler = new ErrorHandler();

    public SignController() {
    }

    @FXML
    public void initialize() {
        // Create the context menu
        contextMenu = new ContextMenu();
        MenuItem optionLight = new MenuItem("Tema Claro");
        MenuItem optionDark = new MenuItem("Tema Oscuro");
        MenuItem optionRetro = new MenuItem("Retro");
        contextMenu.getItems().addAll(optionLight, optionDark, optionRetro);

        // Show context menu on right click
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

        // Hide context menu on left click outside
        loginPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.hide();
            }
        });

        registerPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.hide();
            }
        });

        // Hide context menu when clicking outside of it
        contextMenu.setOnShowing(event -> {
            loginPane.setOnMouseClicked(clickEvent -> {
                if (contextMenu.isShowing() && clickEvent.getButton() == MouseButton.PRIMARY) {
                    contextMenu.hide();
                }
            });

            registerPane.setOnMouseClicked(clickEvent -> {
                if (contextMenu.isShowing() && clickEvent.getButton() == MouseButton.PRIMARY) {
                    contextMenu.hide();
                }
            });
        });

        // Bind actions to MenuItems
        optionLight.setOnAction(e -> changeToLightTheme());
        optionDark.setOnAction(e -> changeToDarkTheme());
        optionRetro.setOnAction(e -> changeToRetroTheme());

        // Initialize password reveal buttons
        initializePasswordRevealButtons();
    }

    private void initializePasswordRevealButtons() {
        revealButton.setOnAction(event -> {
            togglePasswordVisibility(passwordField, plainTextField, revealButton, passwordFieldParent);
        });

        revealRegisterButton.setOnAction(event -> {
            togglePasswordVisibility(registerPasswordField, plainRegisterTextField, revealRegisterButton, registerPasswordFieldParent);
        });

        revealConfirmButton.setOnAction(event -> {
            togglePasswordVisibility(confirmPasswordField, plainConfirmTextField, revealConfirmButton, confirmPasswordFieldParent);
        });
    }

    private void togglePasswordVisibility(PasswordField passwordField, TextField plainTextField, Button revealButton, HBox parent) {
        if (revealButton.getText().equals("Mostrar")) {
            passwordField.setVisible(false);
            plainTextField = new TextField(passwordField.getText());
            plainTextField.setVisible(true);
            plainTextField.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-border-color: #888; -fx-border-radius: 10; -fx-pref-width: 200; -fx-min-width: 200; -fx-max-width: 300;");
            parent.getChildren().set(0, plainTextField);
            revealButton.setText("Ocultar");
        } else {
            if (plainTextField != null) {
                passwordField.setText(plainTextField.getText());
                passwordField.setVisible(true);
                parent.getChildren().set(0, passwordField);
                revealButton.setText("Mostrar");
                plainTextField = null;
            }
        }
    }

    private void changeToLightTheme() {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/ui/stylesClaro.css").toExternalForm());
        }
    }

    private void changeToDarkTheme() {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/ui/stylesOscuro.css").toExternalForm());
        }
    }

    private void changeToRetroTheme() {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/ui/stylesRetro.css").toExternalForm());
        }
    }

    @FXML
    private void handleLogin() {
        String email = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (errorHandler.autenticar(email, password)) {
                messageLabel.setText("¡Inicio de sesión exitoso!");
            }
        } catch (Exception e) {
            errorHandler.handleGeneralException(e, messageLabel);
        }
    }

    @FXML
    private void handleRegister() {
        // ... (your existing registration logic)
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

    private boolean esCorreoValido(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean esContraseñaFuerte(String password) {
        return password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*");
    }
}
