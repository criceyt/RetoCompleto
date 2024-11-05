package exceptions;

/**
 * Excepción personalizada para manejar el caso cuando se alcanza el número máximo 
 * de clientes permitidos para conectarse al servidor.
 * 
 * Se utiliza para notificar que no se puede aceptar más conexiones debido a que 
 * ya se ha alcanzado el límite máximo de clientes permitidos.
 * 
 * @author oier
 */
public class ErrorMaxClientes extends Exception {

    /**
     * Constructor sin parámetros que inicializa la excepción con un mensaje 
     * predeterminado que indica que se ha alcanzado el número máximo de clientes.
     */
    public ErrorMaxClientes() {
        // Llamada al constructor de la clase base (Exception) con un mensaje predeterminado
        super("Error: Se ha alcanzado el máximo de clientes que se pueden conectar.");
    }
}

