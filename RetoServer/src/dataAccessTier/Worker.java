package dataAccessTier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import libreria.Mensaje;
import libreria.Request;

/**
 *
 * @author gorka
 */
public class Worker implements Runnable {

    //private Signable signable;  pa q se usa
    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());
    private final String clave = "abcd";
    AplicattionServer apServer;

    ServerSocket server = null;
    Socket socket = null;
    DAO dao = null;
    ObjectInputStream entrada = null;
    ObjectOutputStream salida = null;
    int numeroCliente;
    //Crear constructor para worker y que tenga un parametro socket y guardarlo en un atributo (socket)


    Worker(Socket socketCliente, int numeroCliente, DAO dao) {
        this.socket = socketCliente;
        this.numeroCliente = numeroCliente;
        this.dao = dao;
    }


    @Override
    public void run() {
        try {
            // Inicializar los streams de entrada y salida
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            // Recibir el objeto Usuario enviado por el cliente
            Mensaje mensaje = (Mensaje) entrada.readObject();
            LOGGER.info("Usuario recibido del cliente " + numeroCliente + ": " + mensaje.getRq());

            // Enviar una confirmación de recepción al cliente
            if(mensaje.getRq().equals(Request.SIGN_UP_REQUEST)) {
                dao.singUp(mensaje);
                salida.writeObject(mensaje);
            } else {
                dao.signIn(mensaje);     
                System.out.println("Mensaje recibido: " + mensaje.getRq());
                salida.writeObject(mensaje);
            }         
            apServer.decrementarClientes();

        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe("Error al procesar el cliente " + numeroCliente + ": " + e.getMessage());
        } finally {
            // Cerrar el socket y los streams
            finalizar();
        }
    }
    
    public void finalizar() {
        try {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null) {
                socket.close();
            }
            
            
            
            
        } catch (IOException e) {
            LOGGER.severe("Error al cerrar recursos del cliente " + numeroCliente + ": " + e.getMessage());
        }
    }

}

