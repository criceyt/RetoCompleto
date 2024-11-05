package exceptions;

/**
 * Excepción personalizada para manejar errores generales en el servidor.
 * 
 * Se utiliza para representar errores no específicos que ocurren en el servidor, 
 * los cuales no son cubiertos por otras excepciones más específicas.
 * 
 * @author oier
 */
public class ErrorGeneral extends Exception {

    /**
     * Constructor sin parámetros que inicializa la excepción con un mensaje 
     * predeterminado que indica que ha ocurrido un error general en el servidor.
     */
    public ErrorGeneral() {
        // Llama al constructor de la clase base (Exception) con un mensaje predeterminado
        super("Error general en el servidor.");
    }
}

