package libreria;

/**
 * Enumeración que define los posibles tipos de solicitudes y respuestas que se
 * pueden manejar entre el servidor y el cliente.
 *
 * Esta clase enum contiene las diferentes acciones que un cliente puede
 * solicitar al servidor, como iniciar sesión, registrarse, o respuestas de
 * error relacionadas con el estado del usuario o el sistema.
 *
 * @author gorka
 */
public enum Request {

    // Solicitud de inicio de sesión 
    SIGN_IN_REQUEST,
    // Solicitud de registro de un nuevo usuario 
    SIGN_UP_REQUEST,
    // Respuesta exitosa para el inicio de sesión 
    SIGN_IN_EXITOSO,
    // Error general del servidor 
    ERROR_GENERAL,
    // El usuario no está activo 
    USUARIO_NO_ACTIVO,
    // El usuario no existe en el sistema 
    USUARIO_INEXISTENTE,
    // Error: El correo del usuario ya está registrado 
    ERROR_USUARIO_YA_EXISTE,
    // Error: Se ha alcanzado el número máximo de clientes conectados 
    ERROR_MAX_CLIENTES
}
