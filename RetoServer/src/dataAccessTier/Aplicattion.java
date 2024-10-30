package dataAccessTier;

import exceptions.ErroMaxClientes;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gorka
 */
public class Aplicattion {

    private static int puerto;
    private static final Logger LOGGER = Logger.getLogger(Aplicattion.class.getName());

    private static int getMaxCon() {
        ResourceBundle bundle = ResourceBundle.getBundle("dataAccessTier.conexion");
        String recogerMaxCon = bundle.getString("maxCon");
        return Integer.valueOf(recogerMaxCon);
    }

    ServerSocket serverSocket = null;
    Socket socketCliente = null;

    public static void main(String[] args) {
        cargarPuerto();
        iniciar();
    }

    private static void cargarPuerto() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("libreria.puerto");
            String recogerPuerto = bundle.getString("PUERTO");
            puerto = Integer.parseInt(recogerPuerto);

        } catch (NumberFormatException e) {
            LOGGER.severe("El puerto en el archivo de propiedades, no es un puerto valido" + e.getMessage());
            puerto = 16700;
        }
    }

    private static void iniciar() {

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            DAO dao = (DAO) ServerFactory.getSignable();
            int numeroCliente = 1;
            while (true) {
                LOGGER.info("Esperando conexion del cliente " + numeroCliente);
                Socket socketCliente = serverSocket.accept();
                LOGGER.info("Cliente " + numeroCliente + " conectado");
                if (numeroCliente <= getMaxCon()) {
                    Worker worker = new Worker(socketCliente, numeroCliente, dao);
                    new Thread(worker).start();
                    numeroCliente++;
                } else {
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                    throw new ErroMaxClientes();
                }

            }
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

}
