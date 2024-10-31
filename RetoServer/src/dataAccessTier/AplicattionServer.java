package dataAccessTier;

import exceptions.ErrorMaxClientes;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Mensaje;
import libreria.Request;

/**
 *
 * @author gorka
 */
public class AplicattionServer {

    private static int puerto;
    private static final Logger LOGGER = Logger.getLogger(AplicattionServer.class.getName());
    private static int numeroClientesConectados = 0;
    private static ServerSocket serverSocket = null;
    private static Socket socketCliente = null;
    private static ObjectOutputStream salida;
    private static Mensaje mensaje = null;

    public static void main(String[] args) {
        cargarPuerto();
        try {
            iniciar();
        } catch (IOException ex) {
            Logger.getLogger(AplicattionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void iniciar() throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            DAO dao = (DAO) ServerFactory.getSignable();
            mensaje = new Mensaje();
            while (true) {
                LOGGER.info("Esperando conexion del cliente " + numeroClientesConectados + 1);
                Socket socketCliente = serverSocket.accept();
                salida = new ObjectOutputStream(socketCliente.getOutputStream());
                LOGGER.info("Cliente " + numeroClientesConectados + 1 + " conectado");
                if (numeroClientesConectados < getMaxCon()) {
                    Worker worker = new Worker(socketCliente, salida, dao);
                    new Thread(worker).start();
                    numeroClientesConectados++;
                } else {
                    mensaje.setRq(Request.ERROR_MAX_CLIENTES);
                    throw new ErrorMaxClientes();
                }
            }
        } catch (ErrorMaxClientes e) {
            salida.writeObject(mensaje);         
        } catch (Exception e) {
            LOGGER.severe("Fallo al intentar crear el socket del servidor" + e.getMessage());
        }
    }

    public void finalizar() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }

            if (socketCliente != null) {
                socketCliente.close();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOGGER.severe("Error al intentar cerrar algun socket");
        }
    }

    private static int getMaxCon() {
        ResourceBundle bundle = ResourceBundle.getBundle("dataAccessTier.conexion");
        String recogerMaxCon = bundle.getString("maxCon");
        return Integer.valueOf(recogerMaxCon);
    }

    public synchronized void decrementarClientes() {
        numeroClientesConectados--;
        LOGGER.info("Número de clientes conectados: " + numeroClientesConectados);
    }

    private static void cargarPuerto() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("libreria.puerto");
            String recogerPuerto = bundle.getString("PUERTO");
            puerto = Integer.parseInt(recogerPuerto);

        } catch (NumberFormatException e) {
            LOGGER.severe("El puerto en el archivo de propiedades, no es un puerto valido" + e.getMessage());
        }
    }

}
