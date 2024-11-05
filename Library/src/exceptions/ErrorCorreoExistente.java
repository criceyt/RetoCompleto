package exceptions;

/**
 * Excepción personalizada para manejar los errores cuando se intenta registrar un usuario
 * con un correo electrónico que ya está registrado en el sistema.
 *
 * @author oier
 */
public class ErrorCorreoExistente extends Exception {

    /**
     * Constructor sin parámetros que inicializa la excepción con un mensaje predeterminado.
     * El mensaje indica que el correo proporcionado ya está registrado en el sistema.
     */
    public ErrorCorreoExistente() {
        // Llamada al constructor de la clase base (Exception) con un mensaje de error específico
        super("Error: El correo que quiere introducir está ya registrado.");
    }
}

