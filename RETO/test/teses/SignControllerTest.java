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
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import userInterfaceTier.Application;

/**
 *
 * @author oier
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Application().start(stage);
    }

    @Test
    public void a_testLoginExitoso() {

        // TEST DE INICIO DE SESION
        // Test de campos vacios
        clickOn("#loginButton");

        verifyThat("No hay ningún campo rellenado.", isVisible());
        clickOn("Aceptar");

        // Test de Campo de gmail vacio
        clickOn("#usernameField");
        write("p9rueba@gmail.com");
        clickOn("#loginButton");

        verifyThat("La contraseña no puede estar vacío.", isVisible());
        clickOn("Aceptar");

        // Test de campo Password vacio
        clickOn("#usernameField");
        eraseText(25);
        clickOn("#passwordFieldParent");
        write("12345678Aa");
        clickOn("#loginButton");

        verifyThat("El email no puede estar vacío.", isVisible());
        clickOn("Aceptar");

        // Test de user Inexistente
        clickOn("#usernameField");
        write("p9rueba@gmail.com");
        clickOn("#loginButton");

        verifyThat("Error: El usuario no ha sido encontrado (NO EXISTE)", isVisible());
        clickOn("Aceptar");

    }

    @Test
    public void b_testRegistrarse() {

        // TEST DE REGISTARSE BIEN
        // Antes de inicar debe de esperar un poco a que abra el desplegable
        clickOn("#registerButton");
        try {
            // Test de Registro
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(SignControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Test de Campos vacios
        clickOn("#handleRegister");
        verifyThat("No hay ningún campo rellenado.", isVisible());
        clickOn("Aceptar");

        // Test de error de campos
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

        //Como verifico un arry de errores
        clickOn("Aceptar");

        // Test de Registro correcto
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
        write("p9rueba@gmail.com");
        clickOn("#registerPasswordFieldParent");
        eraseText(4);
        write("12345678Aa");
        clickOn("#revealRegisterButton");
        clickOn("#confirmPasswordFieldParent");
        eraseText(10);
        write("12345678Aa");
        clickOn("#handleRegister");

        // Como verifico que el user se ha introducido bien
        // Test de Correo repetido
        clickOn("#nombreyApellidoField");
        write("Javi Profesor");
        clickOn("#ciudadField");
        write("Bilbo");
        clickOn("#codigoPostalField");
        write("48000");
        clickOn("#direccionField");
        write("Gran Via");
        clickOn("#emailField");
        write("p9rueba@gmail.com");
        clickOn("#registerPasswordFieldParent");
        write("12345678Aa");
        clickOn("#confirmPasswordFieldParent");
        write("12345678Aa");
        clickOn("#handleRegister");

        verifyThat("Error el correo que quiere introducir esta ya registrado", isVisible());
        clickOn("Aceptar");
        
        clickOn("#backToLoginButton");

        // TEST PARA VER SI EL INICIO DE SESION ES EXITOSO
        // Cada vez que se cambie de desplegable hay que esperar un poco para que se pongan los TextFiel com o visibles
        try {
            // Test de Registro
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(SignControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Se Registra
        clickOn("#usernameField");
        write("p9rueba@gmail.com");
        clickOn("#passwordFieldParent");
        write("12345678Aa");
        clickOn("#loginButton");

        verifyThat("#logoutButton", isVisible());
        clickOn("#logoutButton");

    }

   

    @Test
    public void d_testIniciarInactivo() {

        // TEST PARA REGISTAR Y INICAR UN USER INACTIVO
        clickOn("#registerButton");
        // Hay que esperar un poco porque si no encuentra los elemento dentro del desplegable
        try {
            // Test de Registro
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(SignControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        clickOn("#nombreyApellidoField");
        write("Usuario No Activo");
        clickOn("#ciudadField");
        write("da igual la cuidad");
        clickOn("#codigoPostalField");
        write("11111");
        clickOn("#direccionField");
        write("Prueba");
        clickOn("#emailField");
        write("1usuarioNoActivo@gmail.com");
        clickOn("#registerPasswordFieldParent");
        write("12345678Aa");
        clickOn("#revealRegisterButton");
        clickOn("#confirmPasswordFieldParent");
        write("12345678Aa");
        clickOn("#activoCheckBox");
        clickOn("#handleRegister");

        // Vamos a inciar sesion y a verificar que no esta activo
        clickOn("#backToLoginButton");
        // Esperamos...
        try {
            // Test de Registro
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(SignControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        clickOn("#usernameField");
        eraseText(27);
        write("1usuarioNoActivo9@gmail.com");
        clickOn("#passwordFieldParent");
        eraseText(10);
        write("12345678Aa");
        clickOn("#loginButton");

        verifyThat("Error: El usuario no esta Activo", isVisible());
    }
}