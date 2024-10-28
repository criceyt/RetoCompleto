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

        Parent root = FXMLLoader.load(getClass().getResource("/ui/FXMLLogin.fxml"));
        
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/ui/stylesOscuro.css").toExternalForm());
        
        
        stage.setWidth(900);
        stage.setHeight(780);
        
        
        stage.setTitle("Sign up & Sign in");
        
       
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}