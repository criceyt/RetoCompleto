package reto;

import businesslogic.UsuarioService;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.FXMLLoginController;

/**
 * Main application class for the RETO application.
 * It initializes the JavaFX application and loads the login screen.
 * 
 * @author Ekain
 */
public class RETO extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/FXMLLogin.fxml"));
            Parent root = loader.load();


            FXMLLoginController controller = loader.getController();


            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Login"); 
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
         
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
