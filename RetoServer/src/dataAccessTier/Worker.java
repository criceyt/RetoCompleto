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
 *
 * @author gorka
 */
public class Worker implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());

    ServerSocket server;
    Socket socket;
    DAO dao;
    ObjectInputStream entrada;
    ObjectOutputStream salida;
    
    //Crear constructor para worker y que tenga un parametro socket y guardarlo en un atributo (socket)

    Worker(Socket socketCliente, ObjectOutputStream  salida, DAO dao) {
        this.socket = socketCliente;
        this.salida = salida;
        this.dao = dao;
    }

    @Override
    public void run() {
        try {
            entrada = new ObjectInputStream(socket.getInputStream());
            // Recibir el objeto Usuario enviado por el cliente
            Mensaje mensaje = (Mensaje) entrada.readObject();
            LOGGER.info("Usuario recibido del cliente :"   + mensaje.getRq());

            // Enviar una confirmación de recepción al cliente
            if (mensaje.getRq().equals(Request.SIGN_UP_REQUEST)) {
                try {
                    dao.singUp(mensaje);

                } catch (ErrorCorreoExistente ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.ERROR_USUARIO_YA_EXISTE);
                } catch (ErrorGeneral ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.ERROR_GENERAL);
                }

            } else {
                try {
                    dao.signIn(mensaje);
                } catch (ErrorUsuarioNoActivo ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.USUARIO_NO_ACTIVO);
                } catch (ErrorUsuarioInexistente ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.USUARIO_INEXISTENTE);
                } catch (ErrorGeneral ex) {
                    Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    mensaje.setRq(Request.ERROR_GENERAL);
                }

            }
            System.out.println("Mensaje recibido: " + mensaje.getRq());
            salida.writeObject(mensaje);

        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe("Error al procesar el cliente " + ": " + e.getMessage());
        } finally {
            // Cerrar el socket y los streams
            finalizar();
            decrementarClientes();
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
            LOGGER.severe("Error al cerrar recursos del cliente " + ": " + e.getMessage());
        }
    }

}
