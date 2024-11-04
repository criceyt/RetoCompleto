package userInterfaceTier;

import exceptions.ErrorGeneral;
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
import static javafx.application.Application.launch;
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
import ui.FXMLDashboardController;

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

    @FXML
    private void showRegister() {
        transitionToRegister();
    }

    @FXML
    private void showLogin() {
        transitionToLogin();
    }

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

    // Evento de Boton de Inicio de Sesion
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
            new Alert(Alert.AlertType.ERROR, "La contraseña no puede estar vacío.", ButtonType.OK).showAndWait();
        }

        if (email.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "El email no puede estar vacío.", ButtonType.OK).showAndWait();
        }

        if (!esCorreoValido(email)) {
            //errores.add("El correo electrónico no tiene un formato válido.");
        }

        if (!errores.isEmpty()) {
            String mensajeErrores = String.join("\n", errores);
            errorHandler.handleGeneralException(new Exception(mensajeErrores), messageLabel);
            return; // Salimos del método si hay errores
        }

        try {
            // Autenticar usuario
            if (errorHandler.autenticar(email, password)) {
                Usuario usuario = new Usuario(email, password);
                Mensaje mensaje = new Mensaje(usuario, Request.SIGN_IN_REQUEST);
                Signable a = ClientFactory.getSignable();
                a.signIn(mensaje);
                //       messageLabel.setText("¡Inicio de sesión exitoso!");
                //      System.out.println("Usuario autenticado: " + email);
            }
        } catch (ErrorUsuarioNoActivo ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        } catch (ErrorUsuarioInexistente ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        } catch (ErrorGeneral e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        } catch (Exception ex) {
            Logger.getLogger(SignController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Evento de Boton de Registrase
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
            Mensaje mensaje = new Mensaje(usuario, Request.SIGN_UP_REQUEST);
            Signable a = ClientFactory.getSignable();
            a.singUp(mensaje); //Hay que cambiar el nombre a signUp
            limpiarCamposRegistro();
        } catch (ErrorGeneral ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
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

    // Metodo que cierra el SignIn y abre el SignUp
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

    // Metodo que cierra el SignUp y Abre el SignIn
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

// Metodo para abrir la vista de Iniciode Sesion
    public static void abrirVista() {
        try {
            // Cargar el DOM de la vista FXML
            FXMLLoader loader = new FXMLLoader(SignController.class.getResource("/ui/FXMLDashboard.fxml"));
            Parent root = loader.load();
            // Crear nueva escena
            Scene scene = new Scene(root);
            FXMLDashboardController controller = loader.getController();
            controller.setScene(scene);
            // Añadir el CSS por defecto (Oscuro)
            scene.getStylesheets().add(SignController.class.getResource("/ui/stylesLogout_Oscuro.css").toExternalForm());

            // Crear un menú contextual con las opciones de temas
            ContextMenu themeMenu = createThemeMenu(scene);

            // Añadir un listener para abrir el menú con clic derecho
            root.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() == MouseButton.SECONDARY) { // Clic derecho
                    themeMenu.show(root, event.getScreenX(), event.getScreenY());
                }
            });
            controller.loadDefaultStyles();
            // Crear un nuevo stage
            Stage stage = new Stage();
            stage.setTitle("Ventana Sesion Iniciada");
            stage.setWidth(900);
            stage.setHeight(700);
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);// Bloquea la ventana previa
            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(SignController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // CREACION DEL MENU CONTEXTUAL
    private static ContextMenu createThemeMenu(Scene scene) {
        ContextMenu menu = new ContextMenu();

        // Crear los elementos del menú para cada tema (nombres)
        MenuItem darkTheme = new MenuItem("Tema Oscuro");
        MenuItem lightTheme = new MenuItem("Tema Claro");
        MenuItem retroTheme = new MenuItem("Tema Retro");

        // Añadir acciones a cada elemento del menú
        darkTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Oscuro.css", scene));
        lightTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Claro.css", scene));
        retroTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Retro.css", scene));

        // Añadir los elementos al menú
        menu.getItems().addAll(darkTheme, lightTheme, retroTheme);

        // Retorna el menu
        return menu;
    }

    // Método para cambiar el tema
    private static void changeTheme(String themeFile, Scene scene) {
        // Limpiar estilos actuales y cargar el nuevo CSS
        scene.getStylesheets().clear();
        scene.getStylesheets().add(SignController.class.getResource(themeFile).toExternalForm());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // METODOS PARA CAMBIAR LA ESCENA DE FONDO
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Meotodo para cambiar al tema Blanco
    private void changeToLightTheme() {

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
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/ui/stylesOscuro.css").toExternalForm());
        } else {
            System.out.println("La escena es null");
        }
    }

    // Método para cambiar al tema retro
    private void changeToRetroTheme() {
        Scene scene = loginPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear(); // Limpiar estilos existentes
            scene.getStylesheets().add(getClass().getResource("/ui/stylesRetro.css").toExternalForm());
        } else {
            System.out.println("La escena es null");
        }
    }
}