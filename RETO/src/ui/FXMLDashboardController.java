package ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane; // Importa AnchorPane

public class FXMLDashboardController {

    @FXML
    private AnchorPane rootPane; // Esto debería funcionar ahora con la importación correcta

    private Scene scene;

    @FXML
    public void initialize() {
        // Configura la escena a partir del AnchorPane rootPane
        scene = rootPane.getScene();

        // Crear un menú contextual con opciones de temas
        ContextMenu themeMenu = createThemeMenu();

        // Añadir un listener para abrir el menú con clic derecho en esta ventana
        rootPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Clic derecho
                themeMenu.show(rootPane, event.getScreenX(), event.getScreenY());
            }
        });
    }

    // Método para crear el menú contextual con opciones de temas
    private ContextMenu createThemeMenu() {
        ContextMenu menu = new ContextMenu();

        // Crear los elementos del menú para cada tema
        MenuItem darkTheme = new MenuItem("Tema Oscuro");
        MenuItem lightTheme = new MenuItem("Tema Claro");
        MenuItem retroTheme = new MenuItem("Tema Retro");

        // Añadir acciones a cada elemento del menú
        darkTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Oscuro.css"));
        lightTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Claro.css"));
        retroTheme.setOnAction(event -> changeTheme("/ui/stylesLogout_Retro.css"));

        // Añadir los elementos al menú
        menu.getItems().addAll(darkTheme, lightTheme, retroTheme);

        return menu;
    }

    // Método para cambiar el tema solo en esta ventana
    private void changeTheme(String themeFile) {
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(themeFile).toExternalForm());
        }
    }
}
