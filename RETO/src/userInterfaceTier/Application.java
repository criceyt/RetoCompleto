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
<<<<<<< HEAD
    public void start(Stage stage) throws Exception{
            // Cargar DOM de la vista FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/FXMLLogin.fxml"));
            Parent root = (Parent)loader.load();
          
            // Creamos una escena con nuestra vista
            Scene scene = new Scene(root);
            // Asignamos una altura y anchura
            stage.setWidth(900); 
            stage.setHeight(700); 
            // Ponemos titulo a la app
            stage.setTitle("Sing up & sing in");
            // Poner la escena en el escenario
            stage.setScene(scene);
            stage.show();
=======
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
>>>>>>> 4c445646155a8151a56fe55afa31a72994417d95
    }

    public static void main(String[] args) {
        launch(args);
    }
}