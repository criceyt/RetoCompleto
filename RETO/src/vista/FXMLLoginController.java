package vista;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class FXMLLoginController {

    @FXML
    private VBox loginPane;

    @FXML
    private VBox registerPane;

    @FXML
    private Label messageLabel;

    @FXML
    private VBox messagePane; 

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField registerUsernameField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private void handleLogin() {
        
        System.out.println("Iniciar sesión...");
    }

    @FXML
    private void handleRegister() {
       
        System.out.println("Registrarse...");
    }

    @FXML
    private void showRegister() {
        transitionToRegister(); 
    }

    @FXML
    private void showLogin() {
        transitionToLogin(); 
    }



    private void transitionToRegister() {
        loginPane.setVisible(false); 
        registerPane.setVisible(true);
        registerPane.setTranslateX(loginPane.getWidth()); 

        TranslateTransition transitionIn = new TranslateTransition(Duration.millis(300), registerPane);
        transitionIn.setToX(0);
        transitionIn.play(); 

        
        TranslateTransition messageTransition = new TranslateTransition(Duration.millis(300), messagePane);
        messageTransition.setFromX(0); 
        messageTransition.setToX(-loginPane.getWidth());
        messageTransition.play(); 

       
    }

    private void transitionToLogin() {
        registerPane.setVisible(false); 
        loginPane.setVisible(true);
        loginPane.setTranslateX(-loginPane.getWidth()); 

        TranslateTransition transitionIn = new TranslateTransition(Duration.millis(300), loginPane);
        transitionIn.setToX(0); 
        transitionIn.play();

        // Animar el mensaje hacia la derecha
        TranslateTransition messageTransition = new TranslateTransition(Duration.millis(300), messagePane);
        messageTransition.setFromX(0);
        messageTransition.setToX(loginPane.getWidth());
        messageTransition.play();

        
    }
}
