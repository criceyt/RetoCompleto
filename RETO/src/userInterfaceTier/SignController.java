package userInterfaceTier;

import exceptions.ErrorGeneral;
import exceptions.ErrorMaxClientes;
import exceptions.ErrorUsuarioInexistente;
import exceptions.ErrorUsuarioNoActivo;
import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import libreria.Mensaje;
import libreria.Request;

/**
 * Controlador para el inicio de sesión y registro de usuarios. Este controlador
 * gestiona la lógica de la interfaz gráfica de usuario para el inicio de sesión
 * y el registro, incluidos los cambios de tema, la visibilidad de las
 * contraseñas y la gestión de errores.
 *
 * @author Ekain
 * @author Gorka
 * @author Oier
 */
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

    /**
     * Muestra la vista de registro y oculta la vista de inicio de sesión.
     */
    @FXML
    private void showRegister() {
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

    /**
     * Muestra la vista de inicio de sesión y oculta la vista de registro.
     */
    @FXML
    private void showLogin() {
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

    private ContextMenu contextMenu;
    private boolean isDarkTheme = true;

    private ErrorHandler errorHandler = new ErrorHandler();

    public SignController() {
    }

    /**
     * Inicializa el controlador y configura el menú contextual y los eventos de
     * los botones.
     */
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

    /**
     * Muestra el menú contextual para cambiar entre temas.
     *
     * @param event El evento del mouse que activó el menú contextual.
     */
    private void showContextMenu(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            contextMenu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }
    }

    /**
     * Cambia el tema a claro.
     *
     * @param e El evento de acción.
     */
    private void changeToLightTheme(ActionEvent e) {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/ui/stylesClaro.css").toExternalForm());
        } else {
            System.out.println("La escena es null");
        }
    }

    /**
     * Cambia el tema a oscuro.
     *
     * @param e El evento de acción.
     */
    private void changeToDarkTheme(ActionEvent e) {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/ui/stylesOscuro.css").toExternalForm());
        } else {
            System.out.println("La escena es null");
        }
    }

    /**
     * Cambia el tema a retro.
     *
     * @param e El evento de acción.
     */
    private void changeToRetroTheme(ActionEvent e) {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/ui/stylesRetro.css").toExternalForm());
        } else {
            System.out.println("La escena es null");
        }
    }

    /**
     * Alterna la visibilidad de la contraseña en el campo de contraseña.
     *
     * @param event El evento de acción.
     */
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

    /**
     * Alterna la visibilidad de la contraseña en el campo de registro.
     *
     * @param event El evento de acción.
     */
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

    /**
     * Alterna la visibilidad de la contraseña en el campo de confirmación de
     * contraseña.
     *
     * @param event El evento de acción.
     */
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

    @FXML
    private void handleLogin() {
        String email = usernameField.getText();
        String password = passwordField.getText();
        List<String> errores = new ArrayList<>();

        // Verifica si los campos de email y contraseña están vacíos
        if (email.isEmpty() && password.isEmpty()) {
            errorHandler.handleGeneralException(new Exception("No hay ningún campo rellenado."), messageLabel);
            return;
        }

        // Valida si la contraseña está vacía
        if (password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "La contraseña no puede estar vacía.", ButtonType.OK).showAndWait();
        }

        // Valida si el email está vacío
        if (email.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "El email no puede estar vacío.", ButtonType.OK).showAndWait();
        }

        // Valida si el formato del correo electrónico es válido
        if (!esCorreoValido(email)) {
            // errores.add("El correo electrónico no tiene un formato válido.");
        }

        // Si hay errores, los muestra
        if (!errores.isEmpty()) {
            String mensajeErrores = String.join("\n", errores);
            errorHandler.handleGeneralException(new Exception(mensajeErrores), messageLabel);
            return;
        }

        try {
            // Intenta autenticar al usuario
            if (errorHandler.autenticar(email, password)) {
                Usuario usuario = new Usuario(email, password);
                Mensaje mensaje = new Mensaje(usuario, Request.SIGN_IN_REQUEST);
                Signable a = ClientFactory.getSignable();
                a.signIn(mensaje);
                try {
                    FXMLLoader loader = new FXMLLoader(SignController.class.getResource("/ui/FXMLDashboard.fxml"));
                    Parent root = loader.load();

                    Scene scene = new Scene(root);
                    FXMLDashboardController controller = loader.getController();
                    controller.setScene(scene);

                    scene.getStylesheets().add(SignController.class.getResource("/ui/stylesLogout_Oscuro.css").toExternalForm());

                    controller.loadDefaultStyles();
                    controller.init();

                    Stage stage = new Stage();
                    stage.setTitle("Ventana Sesion Iniciada");
                    stage.setWidth(900);
                    stage.setHeight(700);
                    stage.setScene(scene);

                    stage.initModality(Modality.APPLICATION_MODAL);

                    stage.showAndWait();
                } catch (IOException ex) {
                    Logger.getLogger(SignController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ErrorUsuarioNoActivo ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        } catch (ErrorUsuarioInexistente ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        } catch (ErrorMaxClientes e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        } catch (ErrorGeneral e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        } catch (Exception ex) {
            Logger.getLogger(SignController.class.getName()).log(Level.SEVERE, null, ex);
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

        // Verifica si todos los campos están vacíos
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

        // Valida si el formato del correo electrónico es válido
        if (!esCorreoValido(email)) {
            errores.add("El correo electrónico no tiene un formato válido.");
        }

        // Verifica que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            errores.add("Las contraseñas no coinciden.");
        }

        // Verifica si la contraseña es fuerte
        if (!esContraseñaFuerte(password)) {
            errores.add("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula y un número.");
        }

        // Valida el formato del código postal
        if (!codigoPostalTexto.matches("\\d{5}")) {
            errores.add("El código postal debe tener exactamente 5 números.");
        }

        // Si hay errores, los muestra
        if (!errores.isEmpty()) {
            String mensajeErrores = String.join("\n", errores);
            errorHandler.handleGeneralException(new Exception(mensajeErrores), messageLabel);
            return;
        }

        try {
            int codigoPostal = Integer.parseInt(codigoPostalTexto);
            errorHandler.validarYRegistrar(nombreyApellidos, ciudad, codigoPostal, direccion, email, password, confirmPassword, estaActivo);

            Usuario usuario = new Usuario(email, password, nombreyApellidos, direccion, ciudad, codigoPostal, estaActivo);
            Mensaje mensaje = new Mensaje(usuario, Request.SIGN_UP_REQUEST);
            Signable a = ClientFactory.getSignable();
            a.singUp(mensaje);
            nombreyApellidoField.clear();
            ciudadField.clear();
            codigoPostalField.clear();
            direccionField.clear();
            emailField.clear();
            registerPasswordField.clear();
            confirmPasswordField.clear();
            showLogin();
        } catch (ErrorMaxClientes e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        } catch (ErrorGeneral ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        } catch (Exception e) {
            errorHandler.handleGeneralException(e, messageLabel);
        }
    }

    /**
     * Valida si el correo electrónico tiene un formato válido.
     *
     * @param email El correo electrónico a validar.
     * @return true si el correo electrónico es válido, false en caso contrario.
     */
    private boolean esCorreoValido(String email) {
        return email.contains("@") && email.contains(".");
    }

    /**
     * Valida si la contraseña es fuerte.
     *
     * @param password La contraseña a validar.
     * @return true si la contraseña es fuerte, false en caso contrario.
     */
    private boolean esContraseñaFuerte(String password) {
        return password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*");
    }

    /**
     * Crea un menú contextual para cambiar el tema de la aplicación.
     *
     * @param scene La escena en la que se mostrará el menú.
     * @return El menú contextual creado.
     */
    private static ContextMenu createThemeMenu(Scene scene) {
        ContextMenu menu = new ContextMenu();

        MenuItem darkTheme = new MenuItem("Tema Oscuro");
        MenuItem lightTheme = new MenuItem("Tema Claro");
        MenuItem retroTheme = new MenuItem("Tema Retro");

        darkTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Oscuro.css", scene));
        lightTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Claro.css", scene));
        retroTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Retro.css", scene));

        menu.getItems().addAll(darkTheme, lightTheme, retroTheme);

        return menu;
    }

    /**
     * Cambia el tema de la escena de la aplicación.
     *
     * @param themeFile El archivo de estilo del tema a aplicar.
     * @param scene La escena a la que se le aplicará el nuevo tema.
     */
    private static void changeTheme(String themeFile, Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(SignController.class.getResource(themeFile).toExternalForm());
    }
}
