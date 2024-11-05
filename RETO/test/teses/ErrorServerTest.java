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
public class ErrorServerTest extends ApplicationTest {
    
     @Override
    public void start(Stage stage) throws Exception {
        new Application().start(stage);

    }
    
    @Test
    public void testErrorServer() {
        
        // Test de server cerrado
        clickOn("#usernameField");
        write("5prueba@gmail.com");
        clickOn("#passwordFieldParent");
        write("12345678Aa");
        clickOn("#loginButton");
        
        verifyThat("Error General en el Server", isVisible());
    }
    
}
