package reto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Ekain
 */
public class RETO extends Application {

    @Override
    public void start(Stage stage) throws Exception {
       
        Parent root = FXMLLoader.load(getClass().getResource("/vista/FXMLLogin.fxml"));

        Scene scene = new Scene(root, 800, 600); 
        stage.setScene(scene);
        stage.setTitle("Aplicación de Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
