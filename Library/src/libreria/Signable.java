package libreria;

import exceptions.ErrorCorreoExistente;
import exceptions.ErrorGeneral;
import exceptions.ErrorMaxClientes;
import exceptions.ErrorUsuarioInexistente;
import exceptions.ErrorUsuarioNoActivo;

/**
 * Interfaz que define los métodos necesarios para la gestión de usuarios 
 * en el sistema, específicamente para el registro y la autenticación.
 * 
 * La interfaz `Signable` establece un contrato que debe ser implementado por
 * cualquier clase que maneje la creación y validación de usuarios. Los métodos
 * que se definen aquí permiten tanto el registro de nuevos usuarios como la
 * autenticación de usuarios existentes.
 * 
 * En ambos métodos, si ocurre algún error durante el proceso (por ejemplo,
 * si el correo ya está registrado, el usuario no existe, etc.), se lanzan
 * excepciones personalizadas que se deben manejar adecuadamente.
 * 
 * @author 2dam
 */
public interface Signable {

    /**
     * Método para registrar un nuevo usuario en el sistema.
     * 
     * Este método toma un objeto `Mensaje` que contiene la información del
     * usuario a registrar y realiza el proceso de alta en la base de datos.
     * 
     * @param mensaje Objeto que contiene los datos del usuario a registrar.
     * @return El objeto `Usuario` que fue registrado.
     * @throws ErrorGeneral Si ocurre un error general durante el proceso de registro.
     * @throws ErrorCorreoExistente Si el correo del usuario ya está registrado en el sistema.
     * @throws ErrorMaxClientes Si se alcanza el límite máximo de clientes permitidos en el sistema.
     */
    public Usuario singUp(Mensaje mensaje) throws ErrorGeneral, ErrorCorreoExistente, ErrorMaxClientes;

    /**
     * Método para iniciar sesión con un usuario existente en el sistema.
     * 
     * Este método toma un objeto `Mensaje` que contiene las credenciales del
     * usuario (correo y contraseña) y verifica si son correctas. Si el usuario
     * está activo, el proceso de inicio de sesión es exitoso.
     * 
     * @param mensaje Objeto que contiene las credenciales del usuario para el inicio de sesión.
     * @return El objeto `Usuario` que ha iniciado sesión correctamente.
     * @throws ErrorGeneral Si ocurre un error general durante el proceso de inicio de sesión.
     * @throws ErrorUsuarioNoActivo Si el usuario no está activo y no puede iniciar sesión.
     * @throws ErrorUsuarioInexistente Si el usuario no existe en el sistema.
     * @throws ErrorMaxClientes Si se alcanza el límite máximo de clientes permitidos en el sistema.
     */
    public Usuario signIn(Mensaje mensaje) throws ErrorGeneral, ErrorUsuarioNoActivo, ErrorUsuarioInexistente, ErrorMaxClientes;
}

