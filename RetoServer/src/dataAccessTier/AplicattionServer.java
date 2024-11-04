package dataAccessTier;

import exceptions.ErrorMaxClientes;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;
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
    private static volatile boolean running = true; // Para controlar el estado del servidor

    public static void main(String[] args) throws ErrorMaxClientes {
        cargarPuerto();
        try {
            System.out.println("Para cerrar el servidor, escriba 'cerrar' ");
            iniciar();
        } catch (IOException ex) {
            Logger.getLogger(AplicattionServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AplicattionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void iniciar() throws IOException, SQLException, ErrorMaxClientes {

        // Iniciar el hilo para escuchar comandos
        new Thread(AplicattionServer::escucharComandos).start();

        serverSocket = new ServerSocket(puerto); 
        DAO dao = (DAO) ServerFactory.getSignable();
        mensaje = new Mensaje();

        while (running) {
            try {
                LOGGER.info("Esperando conexión del cliente " + (numeroClientesConectados + 1));
                Socket socketCliente = serverSocket.accept();
                ObjectOutputStream salida = new ObjectOutputStream(socketCliente.getOutputStream());
                LOGGER.info("Cliente " + (numeroClientesConectados + 1) + " conectado");
                if (numeroClientesConectados < getMaxCon()) {
                    Worker worker = new Worker(socketCliente, salida, dao);
                    new Thread(worker).start();
                    numeroClientesConectados++;
                } else {
                    mensaje.setRq(Request.ERROR_MAX_CLIENTES);
                    salida.writeObject(mensaje);
                    socketCliente.close(); // Cerrar conexión si hay demasiados clientes
                }
            } catch (IOException e) {
                if (!running) {
                    LOGGER.info("El servidor se cerró y no se aceptan más conexiones.");
                } else {
                    LOGGER.severe("Error en el socket del servidor: " + e.getMessage());
                }
            }
        }

        finalizar(); // Asegúrate de cerrar el servidor si se sale del bucle
    }

    private static void escucharComandos() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String comando = scanner.nextLine();
                if ("cerrar".equalsIgnoreCase(comando)) {
                    finalizar(); // Llamar al método para cerrar el servidor
                    break; // Salir del bucle
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error al escuchar comandos: " + e.getMessage());
        }
    }

    public static void finalizar() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                LOGGER.info("Servidor cerrado correctamente.");
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

    public static synchronized void decrementarClientes() {
        numeroClientesConectados--;
        LOGGER.info("Número de clientes conectados: " + numeroClientesConectados + 1 );
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
