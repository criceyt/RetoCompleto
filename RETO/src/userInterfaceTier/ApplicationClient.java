package userInterfaceTier;


import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * Main application class for the RETO application. It initializes the JavaFX
 * application and loads the login screen.
 *
 * @author Ekain
 */
public class ApplicationClient extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/ui/FXMLLogin.fxml"));
        
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/ui/stylesOscuro.css").toExternalForm());
        
        
        stage.setWidth(900);
        stage.setHeight(780);
        
        
        stage.setTitle("Sign up & Sign in");
        
       
        stage.setScene(scene);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume(); // Evita que la ventana se cierre inmediatamente
                mostrarConfirmacionCerrar(stage);
            }
        });
        
        
        stage.show();
    }
    
    private void mostrarConfirmacionCerrar(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cierre");
        alert.setHeaderText("¿Estás seguro de que quieres cerrar?");
        alert.setContentText("Se perderán los cambios no guardados.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                stage.close(); // Cierra la ventana si el usuario confirma
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}