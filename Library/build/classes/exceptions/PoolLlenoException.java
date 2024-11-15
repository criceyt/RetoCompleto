package exceptions;

/**
 * Excepción personalizada para manejar los errores cuando se intenta registrar
 * un usuario con un correo electrónico que ya está registrado en el sistema.
 *
 * @author oier
 */
public class PoolLlenoException extends Exception {

    /**
     * Constructor sin parámetros que inicializa la excepción con un mensaje
     * predeterminado. El mensaje indica que el correo proporcionado ya está
     * registrado en el sistema.
     */
    public PoolLlenoException() {
        // Llamada al constructor de la clase base (Exception) con un mensaje de error específico
        super("No hay conexiones disponibles en el pool.");
    }

    public PoolLlenoException(String message) {
        super(message);
    }
    
}
