package excepciones;

/**
 *
 * @author gorka
 */
public class ErrorGeneral extends Exception {

    public ErrorGeneral() {
        super("Error interno del servidor");
    }
}
