package dataAccessTier;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 *
 * @author gorka
 */
public class AplicattionServer {

    private static int puerto;
    private static final Logger LOGGER = Logger.getLogger(AplicattionServer.class.getName());
    private static int numeroClientesConectados = 0;    
    ServerSocket serverSocket = null;
    Socket socketCliente = null;
    

    public static void main(String[] args) {
        cargarPuerto();
        iniciar();
    }  

    private static void iniciar() {
        
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            DAO dao = (DAO) ServerFactory.getSignable();
            while (true) {
                LOGGER.info("Esperando conexion del cliente " + numeroClientesConectados);
                Socket socketCliente = serverSocket.accept();
                LOGGER.info("Cliente " + numeroClientesConectados + " conectado");
                if(numeroClientesConectados < getMaxCon()){
                    Worker worker = new Worker(socketCliente, numeroClientesConectados, dao);
                    new Thread(worker).start();
                    numeroClientesConectados++;
                }else{
                    //throw new MaximoConexiones;
                    socketCliente.close();
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
    
    
    private static int getMaxCon() {
        ResourceBundle bundle = ResourceBundle.getBundle("dataAccesTier.conexion");
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
            puerto = 16700;
        }
    }
  
}

