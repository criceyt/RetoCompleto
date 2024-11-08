package exceptions;

/**
 * Excepción personalizada para manejar el caso en el que un usuario está
 * inactivo y no puede realizar la acción solicitada (por ejemplo, iniciar
 * sesión).
 *
 * Se utiliza para notificar cuando un usuario intenta realizar una acción (como
 * iniciar sesión) pero su cuenta está desactivada o inactiva.
 *
 * @author oier
 */
public class ErrorUsuarioNoActivo extends Exception {

    /**
     * Constructor sin parámetros que inicializa la excepción con un mensaje
     * predeterminado que indica que el usuario no está activo.
     */
    public ErrorUsuarioNoActivo() {
        // Llamada al constructor de la clase base (Exception) con un mensaje predeterminado
        super("Error: El usuario no está activo.");
    }
}
