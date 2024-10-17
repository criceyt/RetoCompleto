package userInterfaceTier;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Main application class for the RETO application. It initializes the JavaFX
 * application and loads the login screen.
 *
 * @author Ekain
 */
public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar DOM de la vista FXML
        Parent root = FXMLLoader.load(getClass().getResource("/ui/FXMLLogin.fxml"));
        
        // Creamos una escena con nuestra vista
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/ui/stylesOscuro.css").toExternalForm());
        
        // Asignamos una altura y anchura
        stage.setWidth(900);
        stage.setHeight(700);
        
        // Ponemos titulo a la app
        stage.setTitle("Sign up & Sign in");
        
        // Poner la escena en el escenario
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}