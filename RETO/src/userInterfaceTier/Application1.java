package userInterfaceTier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Application1 extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar el DOM de la vista FXML
        Parent root = FXMLLoader.load(getClass().getResource("/ui/FXMLDashboard.fxml"));

        // Crear una escena con nuestra vista
        scene = new Scene(root);

        // Añadir el CSS por defecto (Oscuro)
        scene.getStylesheets().add(getClass().getResource("/ui/stylesLogout_Oscuro.css").toExternalForm());

        // Crear un menú contextual con las opciones de temas
        ContextMenu themeMenu = createThemeMenu();

        // Añadir un listener para abrir el menú con clic derecho
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // Clic derecho
                themeMenu.show(root, event.getScreenX(), event.getScreenY());
            }
        });

        // Configurar la escena y el escenario
        stage.setWidth(900);
        stage.setHeight(700);
        stage.setTitle("Sign up & Sign in");
        stage.setScene(scene);
        stage.show();
    }

    // Método para crear el menú contextual con las opciones de temas
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

    // Método para cambiar el tema
    private void changeTheme(String themeFile) {
        // Limpiar estilos actuales y cargar el nuevo CSS
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(themeFile).toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
