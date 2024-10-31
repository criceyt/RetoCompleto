/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teses;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import userInterfaceTier.Application;


/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Application().start(stage);
        
    }
    
    @Test
    public void testLoginExitoso() {

        // Test de Inicio de Sesion
        
    clickOn("#usernameField");
    write("java@gmail.com");
    clickOn("#loginButton");
    clickOn("Aceptar");
    clickOn("#usernameField");
    eraseText(25);
    clickOn("#passwordFieldParent");
    write("12345678Aa");
    clickOn("#loginButton");
    clickOn("Aceptar");
    clickOn("#usernameField");
    write("java@gmail.com");
    clickOn("#loginButton");
    clickOn("Aceptar");
    
    }

    @Test
    public void testRegistrarse() {

        clickOn("#registerButton");
        try {
            // Test de Registro
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(SignControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        clickOn("#nombreyApellidoField");
        write("32432432");
        clickOn("#ciudadField");
        write("24t4g");
        clickOn("#codigoPostalField");
        write("ebrtbb");
        clickOn("#direccionField");
        write("35v4");
        clickOn("#emailField");
        write("nifn4in4");
        clickOn("#registerPasswordFieldParent");
        write("rfrf");
        clickOn("#revealRegisterButton");
        clickOn("#confirmPasswordFieldParent");
        write("12345678Aa");
        clickOn("#handleRegister");
        clickOn("Aceptar");

        clickOn("#nombreyApellidoField");
        eraseText(8);
        write("Javi Profesor");
        clickOn("#ciudadField");
        eraseText(5);
        write("Bilbo");
        clickOn("#codigoPostalField");
        eraseText(6);
        write("48000");
        clickOn("#direccionField");
        eraseText(4);
        write("Gran Via");
        clickOn("#emailField");
        eraseText(8);
        write("javiProfesor@gmail.com");
        clickOn("#registerPasswordFieldParent");
        eraseText(4);
        write("12345678Aa");
        clickOn("#revealRegisterButton");
        clickOn("#confirmPasswordFieldParent");
        eraseText(10);
        write("12345678Aa");
        clickOn("#handleRegister");

    }
}
