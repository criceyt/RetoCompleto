package dataAccessTier;

import static dataAccessTier.AplicattionServer.decrementarClientes;
import exceptions.ErrorCorreoExistente;
import exceptions.ErrorGeneral;
import exceptions.ErrorUsuarioInexistente;
import exceptions.ErrorUsuarioNoActivo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Mensaje;
import libreria.Request;

/**
 * Clase que representa un "worker" (hilo de trabajo) en el servidor. Cada vez que un cliente se conecta,
 * se crea una instancia de esta clase para manejar la comunicación con ese cliente de manera independiente.
 * 
 * Un worker es responsable de procesar las peticiones de autenticación o registro enviadas por los clientes.
 * 
 * El worker recibe los mensajes del cliente, procesa la solicitud de inicio de sesión o registro, y responde
 * al cliente con un mensaje que indica el resultado de la operación.
 * 
 * @author gorka
 */
public class Worker implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());

    private ServerSocket server;
    private Socket socket;
    private DAO dao;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    
    /**
     * Constructor para crear una instancia de Worker, asignando el socket de cliente, el objeto de salida 
     * y el DAO para interactuar con la base de datos.
     * 
     * @param socketCliente El socket de cliente para la comunicación.
     * @param salida El flujo de salida de datos hacia el cliente.
     * @param dao El objeto DAO que permite interactuar con la base de datos.
     */
    Worker(Socket socketCliente, ObjectOutputStream salida, DAO dao) {
        this.socket = socketCliente;
        this.salida = salida;
        this.dao = dao;
    }

    /**
     * Método ejecutado cuando el hilo es iniciado. Este método es responsable de procesar las solicitudes
     * de los clientes. El worker recibirá un mensaje del cliente, procesará la solicitud (registro o inicio de sesión)
     * y enviará la respuesta correspondiente.
     */
    @Override
    public void run() {
        try {
            entrada = new ObjectInputStream(socket.getInputStream());  // Abrir flujo de entrada para recibir objetos
            // Recibir el objeto Mensaje enviado por el cliente
            Mensaje mensaje = (Mensaje) entrada.readObject();
            LOGGER.info("Usuario recibido del cliente :" + mensaje.getRq());

            // Si la solicitud es un registro de usuario (SIGN_UP_REQUEST)
            if (mensaje.getRq().equals(Request.SIGN_UP_REQUEST)) {
                try {
                    // Intentar registrar el usuario usando el DAO
                    dao.singUp(mensaje);
                } catch (ErrorCorreoExistente ex) {
                    // Manejar el error de correo ya existente
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.ERROR_USUARIO_YA_EXISTE);  // Error de usuario ya existe
                } catch (ErrorGeneral ex) {
                    // Manejar errores generales
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.ERROR_GENERAL);  // Error genérico
                }
            } else {
                // Si la solicitud es un inicio de sesión (signIn)
                try {
                    dao.signIn(mensaje);  // Intentar iniciar sesión con el mensaje
                } catch (ErrorUsuarioNoActivo ex) {
                    // El usuario no está activo
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.USUARIO_NO_ACTIVO);  // Error: Usuario no activo
                } catch (ErrorUsuarioInexistente ex) {
                    // El usuario no existe
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.USUARIO_INEXISTENTE);  // Error: Usuario inexistente
                } catch (ErrorGeneral ex) {
                    // Error general al procesar la solicitud
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.ERROR_GENERAL);  // Error genérico
                }
            }

            // Imprimir el mensaje recibido para verificación
            System.out.println("Mensaje recibido: " + mensaje.getRq());
            // Enviar la respuesta al cliente
            salida.writeObject(mensaje);

        } catch (IOException | ClassNotFoundException e) {
            // Manejar errores al procesar la solicitud del cliente
            LOGGER.severe("Error al procesar el cliente ");
        } finally {
            // Cerrar recursos y notificar que el cliente ha sido procesado
            finalizar();
            decrementarClientes();  // Decrementar el número de clientes conectados
        }
    }

    /**
     * Método para cerrar los recursos utilizados por este worker, como el socket y los flujos de entrada y salida.
     * Se asegura de liberar los recursos una vez que el worker ha terminado de procesar al cliente.
     */
    public void finalizar() {
        try {
            if (entrada != null) {
                entrada.close();  // Cerrar flujo de entrada
            }
            if (salida != null) {
                salida.close();  // Cerrar flujo de salida
            }
            if (socket != null) {
                socket.close();  // Cerrar socket de conexión
            }
        } catch (IOException e) {
            // Manejar errores al cerrar los recursos
            LOGGER.severe("Error al cerrar recursos del cliente ");
        }
    }
}

