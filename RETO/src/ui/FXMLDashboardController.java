package ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FXMLDashboardController {

    @FXML
    private AnchorPane rootPane;

    private Scene scene;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @FXML

    public void initialize() {
        System.out.println("Scene in initialize: " + scene);

        // Crear y configurar el menú contextual
        ContextMenu themeMenu = createThemeMenu();

        rootPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Clic derecho
                themeMenu.show(rootPane, event.getScreenX(), event.getScreenY());
            }
        });
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

        menu.getItems().addAll(darkTheme, lightTheme, retroTheme);
        return menu;
    }

    private void changeTheme(String themeFile) {
        if (scene != null) {
            scene.getStylesheets().clear();
            String stylesheet = getClass().getResource(themeFile).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            // Actualizar el fondo basado en el tema
            if (themeFile.equals("/ui/stylesLogout_Claro.css")) {
                rootPane.setStyle("-fx-background-image: url('/ui/fondoClaro.jpg');");
            } else if (themeFile.equals("/ui/stylesLogout_Oscuro.css")) {
                rootPane.setStyle("-fx-background-image: url('/ui/fondo_(1).jpg');");
            } else if (themeFile.equals("/ui/stylesLogout_Retro.css")) {
                rootPane.setStyle("-fx-background-image: url('/ui/fondoRetro.jpg');");
            }
        } else {
            System.out.println("La escena es null");
        }
    }

    public void loadDefaultStyles() {
        if (scene != null) {
            scene.getStylesheets().clear(); // Limpiar estilos previos si los hay
            String defaultStylesheet = getClass().getResource("/ui/stylesLogout_Oscuro.css").toExternalForm(); // Cambia a tu CSS por defecto
            scene.getStylesheets().add(defaultStylesheet); // Agrega el CSS por defecto
        }
    }

}
