package userInterfaceTier;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
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

    private Scene scene;

    // Método para establecer la escena desde el controlador principal
    public void setScene(Scene scene) {
        this.scene = scene;
        loadDefaultStyles(); // Cargar estilos predeterminados al establecer la escena
    }

    
    public void init() {
        // Crear y configurar el menú contextual
        ContextMenu themeMenu = createThemeMenu();

        // Mostrar el menú contextual al hacer clic derecho en el rootPane
        rootPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Clic derecho
                themeMenu.show(rootPane, event.getScreenX(), event.getScreenY());
            }
        });

        // Configurar la acción del botón de logout
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

        
        darkTheme.setOnAction(event -> {
            changeTheme("/ui/stylesLogout_Oscuro.css");
            menu.hide();
        });
        lightTheme.setOnAction(event -> {
            changeTheme("/ui/stylesLogout_Claro.css");
            menu.hide();
        });
        retroTheme.setOnAction(event -> {
            changeTheme("/ui/stylesLogout_Retro.css");
            menu.hide();
        });

        menu.getItems().addAll(darkTheme, lightTheme, retroTheme);
        return menu; // No se necesita llamar a hide() aquí
    }

    // Método para cambiar el tema de la escena
    private void changeTheme(String themeFile) {
        if (scene != null) {
            scene.getStylesheets().clear();
            String stylesheet = getClass().getResource(themeFile).toExternalForm();
            scene.getStylesheets().add(stylesheet);
        } else {
            System.out.println("La escena es null");
        }
    }

    // Método para cargar los estilos predeterminados al inicio
    public void loadDefaultStyles() {
        if (scene != null) {
            scene.getStylesheets().clear();
            String defaultStylesheet = getClass().getResource("/ui/stylesLogout_Oscuro.css").toExternalForm();
            scene.getStylesheets().add(defaultStylesheet);
        }
    }

    // Método para cerrar la ventana
    private void closeWindow() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close(); 
    }
}
