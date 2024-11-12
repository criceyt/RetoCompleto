package exceptions;

/**
 * Excepción personalizada para manejar el caso en el que un usuario no se
 * encuentra en la base de datos, lo que indica que el usuario no existe.
 *
 * Se utiliza para notificar cuando se intenta acceder a un usuario que no está
 * registrado o no existe en el sistema.
 *
 * @author oier
 */
public class ErrorUsuarioInexistente extends Exception {

    /**
     * Constructor sin parámetros que inicializa la excepción con un mensaje
     * predeterminado que indica que el usuario no ha sido encontrado.
     */
    public ErrorUsuarioInexistente() {
        // Llamada al constructor de la clase base (Exception) con un mensaje predeterminado
        super("Error: El usuario no ha sido encontrado (NO EXISTE).");
    }
}
