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

/**
 * Controlador para la interfaz del dashboard en la aplicación.
 * Maneja la lógica de la interfaz de usuario relacionada con el 
 * panel principal y la selección de temas.
 * @author Oier
 */
public class FXMLDashboardController {

    @FXML
    private AnchorPane rootPane;  // Pane raíz de la interfaz gráfica.

    @FXML
    private Button logoutButton;  // Botón para cerrar sesión.

    private Scene scene;  // Escena actual de la interfaz.

    /**
     * Establece la escena para el controlador y carga los estilos predeterminados.
     * 
     * @param scene la escena que se va a establecer.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
        loadDefaultStyles();
    }

    /**
     * Inicializa el controlador y configura los eventos para el menú de temas
     * y el botón de cierre de sesión.
     */
    public void init() {
        ContextMenu themeMenu = createThemeMenu();

        // Muestra el menú de contexto al hacer clic derecho en el pane raíz.
        rootPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                themeMenu.show(rootPane, event.getScreenX(), event.getScreenY());
            }
        });

        logoutButton.setOnAction(this::handleLogoutButtonAction); // Configura la acción del botón de logout.
    }

    /**
     * Maneja la acción de clic en el botón de cierre de sesión.
     * 
     * @param event el evento de acción.
     */
    private void handleLogoutButtonAction(javafx.event.ActionEvent event) {
        closeWindow();  // Cierra la ventana actual.
    }

    /**
     * Crea un menú contextual que permite seleccionar diferentes temas de la interfaz.
     * 
     * @return el menú contextual de selección de temas.
     */
    private ContextMenu createThemeMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem darkTheme = new MenuItem("Tema Oscuro");
        MenuItem lightTheme = new MenuItem("Tema Claro");
        MenuItem retroTheme = new MenuItem("Tema Retro");

        // Establece las acciones para los ítems del menú de temas.
        darkTheme.setOnAction(event -> {
            changeTheme("/ui/stylesLogout_Oscuro.css");
            menu.hide();  // Cierra el menú después de seleccionar un tema.
        });
        lightTheme.setOnAction(event -> {
            changeTheme("/ui/stylesLogout_Claro.css");
            menu.hide();
        });
        retroTheme.setOnAction(event -> {
            changeTheme("/ui/stylesLogout_Retro.css");
            menu.hide();
        });

        menu.getItems().addAll(darkTheme, lightTheme, retroTheme); // Agrega los ítems al menú.
        return menu;
    }

    /**
     * Cambia el tema de la escena según el archivo de estilo proporcionado.
     * 
     * @param themeFile la ruta del archivo de estilo que se va a aplicar.
     */
    private void changeTheme(String themeFile) {
        if (scene != null) {
            scene.getStylesheets().clear();  // Limpia los estilos actuales.
            String stylesheet = getClass().getResource(themeFile).toExternalForm(); // Obtiene la ruta del nuevo estilo.
            scene.getStylesheets().add(stylesheet);  // Agrega el nuevo estilo a la escena.
        } else {
            System.out.println("La escena es null");  // Mensaje de advertencia si la escena es nula.
        }
    }

    /**
     * Carga los estilos predeterminados para la escena actual.
     */
    public void loadDefaultStyles() {
        if (scene != null) {
            scene.getStylesheets().clear();  // Limpia los estilos actuales.
            String defaultStylesheet = getClass().getResource("/ui/stylesLogout_Oscuro.css").toExternalForm(); // Obtiene el estilo predeterminado.
            scene.getStylesheets().add(defaultStylesheet);  // Aplica el estilo predeterminado a la escena.
        }
    }

    /**
     * Cierra la ventana actual del dashboard.
     */
    private void closeWindow() {
        Stage stage = (Stage) rootPane.getScene().getWindow(); // Obtiene la ventana de la escena actual.
        stage.close();  // Cierra la ventana.
    }
}
