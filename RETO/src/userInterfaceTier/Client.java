package userInterfaceTier;

import exceptions.ErrorCorreoExistente;
import exceptions.ErrorGeneral;
import exceptions.ErrorMaxClientes;
import exceptions.ErrorUsuarioInexistente;
import exceptions.ErrorUsuarioNoActivo;
import libreria.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import libreria.Mensaje;
import static libreria.Request.ERROR_GENERAL;
import static libreria.Request.SIGN_IN_EXITOSO;
import static libreria.Request.USUARIO_INEXISTENTE;
import static libreria.Request.USUARIO_NO_ACTIVO;
import libreria.Usuario;

/**
 * Clase que implementa la interfaz {@link Signable} para gestionar el
 * inicio de sesión y el registro de usuarios a través de un socket.
 * Esta clase maneja la comunicación con un servidor remoto para
 * autenticar a los usuarios y registrar nuevos usuarios.
 * 
 * @author gorka
 */
public class Client implements Signable {

    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    private final String HOST = "127.0.0.1";
    private static int puerto;
    Socket socket = null;
    ObjectInputStream entrada = null;
    ObjectOutputStream salida = null;
    Alert alert;

    /**
     * Registra un nuevo usuario.
     * 
     * @param mensaje objeto que contiene la información del usuario a registrar.
     * @return el usuario registrado.
     * @throws ErrorGeneral si ocurre un error general durante el registro.
     * @throws ErrorCorreoExistente si el correo ya está asociado a un usuario existente.
     * @throws ErrorMaxClientes si se ha alcanzado el número máximo de clientes permitidos.
     */
    @Override
    public Usuario singUp(Mensaje mensaje) throws ErrorGeneral, ErrorCorreoExistente, ErrorMaxClientes {

        try {
            // cargar puerto
            ResourceBundle bundle = ResourceBundle.getBundle("libreria.puerto");
            String recogerPuerto = bundle.getString("PUERTO");
            puerto = Integer.parseInt(recogerPuerto);

            // iniciar
            socket = new Socket(HOST, puerto);

            LOGGER.info("Conexion realizada con el servidor");

            entrada = new ObjectInputStream(socket.getInputStream());

            salida = new ObjectOutputStream(socket.getOutputStream());

            salida.writeObject(mensaje);
            mensaje = (Mensaje) entrada.readObject();
            System.out.println(mensaje.getRq());

            switch (mensaje.getRq()) {
                case ERROR_GENERAL:
                    throw new ErrorGeneral();
                case ERROR_USUARIO_YA_EXISTE:
                    throw new ErrorCorreoExistente();
                case ERROR_MAX_CLIENTES:
                    throw new ErrorMaxClientes();
            }

        } catch (NumberFormatException e) {
            LOGGER.severe("El puerto en el archivo de propiedades, no es un puerto valido" + e.getMessage());
        } catch (IOException e) {
            LOGGER.severe("No se ha podido conectar con el servidor");
            throw new ErrorGeneral();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            finalizar();
        }

        return mensaje.getUser();
    }

    /**
     * Inicia sesión de un usuario.
     * 
     * @param mensaje objeto que contiene la información del usuario para iniciar sesión.
     * @return el usuario autenticado.
     * @throws ErrorGeneral si ocurre un error general durante el inicio de sesión.
     * @throws ErrorUsuarioNoActivo si el usuario no está activo.
     * @throws ErrorUsuarioInexistente si el usuario no existe.
     * @throws ErrorMaxClientes si se ha alcanzado el número máximo de clientes permitidos.
     */
    @Override
    public Usuario signIn(Mensaje mensaje) throws ErrorGeneral, ErrorUsuarioNoActivo, ErrorUsuarioInexistente, ErrorMaxClientes {

        try {
            // cargar puerto

            ResourceBundle bundle = ResourceBundle.getBundle("libreria.puerto");
            String recogerPuerto = bundle.getString("PUERTO");
            puerto = Integer.parseInt(recogerPuerto);

            // iniciar
            socket = new Socket(HOST, puerto);

            LOGGER.info("Conexion realizada con el servidor");

            entrada = new ObjectInputStream(socket.getInputStream());

            salida = new ObjectOutputStream(socket.getOutputStream());

            salida.writeObject(mensaje);
            mensaje = (Mensaje) entrada.readObject();
            System.out.println(mensaje.getRq());

            switch (mensaje.getRq()) {
                case SIGN_IN_EXITOSO:
                    //SignController.abrirVista();
                    break;
                case ERROR_GENERAL:
                    throw new ErrorGeneral();

                case USUARIO_NO_ACTIVO:
                    throw new ErrorUsuarioNoActivo();

                case USUARIO_INEXISTENTE:
                    throw new ErrorUsuarioInexistente();
                case ERROR_MAX_CLIENTES:
                    throw new ErrorMaxClientes();

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorGeneral();
        } catch (NumberFormatException e) {
            LOGGER.severe("El puerto en el archivo de propiedades, no es un puerto valido" + e.getMessage());
        } catch (IOException e) {
            LOGGER.severe("No se ha podido conectar con el servidor, hilos ocupados");
            throw new ErrorGeneral();
        } finally {
            finalizar();
        }
        return mensaje.getUser();
    }

    /**
     * Cierra la conexión con el servidor y libera los recursos utilizados.
     */
    private void finalizar() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
        } catch (IOException e) {
            LOGGER.severe("Error al intentar cerrar" + e.getMessage());
        }
    }
}
