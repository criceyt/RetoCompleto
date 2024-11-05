/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teses;

import javafx.stage.Stage;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import userInterfaceTier.Application;

/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSignIn extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Application().start(stage);
    }

    @Test
    public void a_testSignInNoExitoso() {

        // TEST DE INICIO DE SESION
        // Test de campos vacios
        clickOn("#loginButton");

        verifyThat("No hay ningún campo rellenado.", isVisible());
        clickOn("Aceptar");

        // Test de Campo de gmail vacio
        clickOn("#usernameField");
        write("usuNoExi@gmail.com");
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
        write("usuNoExi@gmail.com");
        clickOn("#loginButton");

        verifyThat("Error: El usuario no ha sido encontrado (NO EXISTE)", isVisible());
        clickOn("Aceptar");

    }

    @Test
    public void b_testSignIpExitoso() {
        // Se Registra
        clickOn("#usernameField");
        write("usuario@gmail.com");
        clickOn("#passwordFieldParent");
        write("12345678Aa");
        clickOn("#loginButton");

        verifyThat("#logoutButton", isVisible());
        clickOn("#logoutButton");
        verifyThat("#usernameField", isVisible());
    }

    @Test
    public void c_testSignInNoActivo() {
        // Iniciar Sesion con user No Activo
        clickOn("#usernameField");
        eraseText(27);
        write("u5suarioNoActivo@gmail.com");
        clickOn("#passwordFieldParent");
        eraseText(10);
        write("12345678Aa");
        clickOn("#loginButton");

        verifyThat("Error: El usuario no esta Activo", isVisible());
    }
}