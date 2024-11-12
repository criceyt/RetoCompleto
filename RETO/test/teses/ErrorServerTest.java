package teses;

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
public class ErrorServerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new ApplicationClient().start(stage);

    }

    @Test
    public void testErrorServer() {

        // Test de server cerrado
        clickOn("#usernameField");
        write("6prueba@gmail.com");
        clickOn("#passwordFieldParent");
        write("12345678Aa");
        clickOn("#loginButton");

        verifyThat("Error general en el servidor.", isVisible());
        clickOn("Aceptar");
    }

}
