package ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLDashboardController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button logoutButton;

    private Label bienvenido;

    private Scene scene;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @FXML
    public void initialize() {

        // Crear y configurar el menú contextual
        ContextMenu themeMenu = createThemeMenu();

        rootPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Clic derecho
                themeMenu.show(rootPane, event.getScreenX(), event.getScreenY());
            }
        });

        logoutButton.setOnAction(this::handleLogoutButtonAction);

    }

    private void handleLogoutButtonAction(javafx.event.ActionEvent event) {
        closeWindow(); // Cerrar la ventana
    }

    private ContextMenu createThemeMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem darkTheme = new MenuItem("Tema Oscuro");
        MenuItem lightTheme = new MenuItem("Tema Claro");
        MenuItem retroTheme = new MenuItem("Tema Retro");

        // Establece las acciones para cambiar el tema
        darkTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Oscuro.css"));
        lightTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Claro.css"));
        retroTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Retro.css"));

        // Configuración para cerrar el menú después de seleccionar un tema
        darkTheme.setOnAction(event -> {
            changeTheme("/ui/stylesLogout_Oscuro.css");
            menu.hide(); // Oculta el menú
        });
        lightTheme.setOnAction(event -> {
            changeTheme("/ui/stylesLogout_Claro.css");
            menu.hide(); // Oculta el menú
        });
        retroTheme.setOnAction(event -> {
            changeTheme("/ui/stylesLogout_Retro.css");
            menu.hide(); // Oculta el menú
        });

        menu.getItems().addAll(darkTheme, lightTheme, retroTheme);
        return menu;
    }

    private void changeTheme(String themeFile) {
        if (scene != null) {
            scene.getStylesheets().clear();
            String stylesheet = getClass().getResource(themeFile).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            String fondo = "";
            switch (themeFile) {
                case "/ui/stylesLogout_Claro.css":
                    fondo = "/ui/fondoClaro.jpg";
                    break;
                case "/ui/stylesLogout_Oscuro.css":
                    fondo = "/ui/fondo_(1).jpg";
                    break;
                case "/ui/stylesLogout_Retro.css":
                    fondo = "/ui/fondoRetro.jpg";
                    break;
            }
            rootPane.setStyle("-fx-background-image: url('" + fondo + "');");
        } else {
            System.out.println("La escena es null");
        }
    }

    public void loadDefaultStyles() {
        if (scene != null) {
            scene.getStylesheets().clear();
            String defaultStylesheet = getClass().getResource("/ui/stylesLogout_Oscuro.css").toExternalForm();
            scene.getStylesheets().add(defaultStylesheet);
        }
    }

    private void closeWindow() {
        // Obtiene el Stage actual desde cualquier nodo, en este caso, rootPane
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close(); // Cierra la ventana
    }
}