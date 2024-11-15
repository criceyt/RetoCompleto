package teses;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import userInterfaceTier.ApplicationClient;

/**
 *
 * @author oier
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSignUp extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new ApplicationClient().start(stage);
    }

    @Test
    public void a_testSignUpExitoso() {

        // TEST DE REGISTARSE BIEN
        // Antes de inicar debe de esperar un poco a que abra el desplegable
        clickOn("#registerButton");
        try {
            // Test de Registro
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestSignUp.class.getName()).log(Level.SEVERE, null, ex);
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

        verifyThat("Ha ocurrido un error inesperado", isVisible());
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
        write("usuarioi@gmail.com");
        clickOn("#registerPasswordFieldParent");
        eraseText(4);
        write("12345678Aa");
        clickOn("#revealRegisterButton");
        clickOn("#confirmPasswordFieldParent");
        eraseText(10);
        write("12345678Aa");
        clickOn("#handleRegister");

        try {
            // Test de Registro
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestSignUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        verifyThat("#loginButton", isVisible());
        
        clickOn("#registerButton");
        
        try {
            // Test de Registro
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestSignUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        write("usuarioi@gmail.com");
        clickOn("#registerPasswordFieldParent");
        write("12345678Aa");
        clickOn("#confirmPasswordFieldParent");
        write("12345678Aa");
        clickOn("#handleRegister");

        verifyThat("Error: El correo que quiere introducir está ya registrado.", isVisible());
        clickOn("Aceptar");

    }

    @Test
    public void testSignUpInactivo() {
        // TEST PARA REGISTAR Y INICAR UN USER INACTIVO
        clickOn("#registerButton");
        // Hay que esperar un poco porque si no encuentra los elemento dentro del desplegable
        try {
            // Test de Registro
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestSignUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Primer registro de User No Activo
        clickOn("#nombreyApellidoField");
        write("Usuario No Activo");
        clickOn("#ciudadField");
        write("da igual la cuidad");
        clickOn("#codigoPostalField");
        write("11111");
        clickOn("#direccionField");
        write("Prueba");
        clickOn("#emailField");
        write("uariioNoActivo@gmail.com");
        clickOn("#registerPasswordFieldParent");
        write("12345678Aa");
        clickOn("#revealRegisterButton");
        clickOn("#confirmPasswordFieldParent");
        write("12345678Aa");
        clickOn("#activoCheckBox");
        clickOn("#handleRegister");

        verifyThat("#usernameField", isVisible());
        // Segundo registro de User Activo

    }
}