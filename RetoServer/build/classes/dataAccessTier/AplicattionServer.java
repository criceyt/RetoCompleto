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
 * Servidor de aplicaciones que acepta conexiones de clientes y gestiona la
 * comunicación con ellos mediante sockets. Permite también cerrar el servidor
 * desde la consola.
 *
 * @author gorka
 */
public class AplicattionServer {

    private static int puerto;
    private static final Logger LOGGER = Logger.getLogger(AplicattionServer.class.getName());
    private static int numeroClientesConectados = 0;
    private static ServerSocket serverSocket = null;
    private static Socket socketCliente = null;
    private static ObjectOutputStream salida; // Flujo de salida para enviar datos al cliente
    private static Mensaje mensaje = null; // Mensaje utilizado para la comunicación con el cliente
    private static volatile boolean running = true; // Indica si el servidor está corriendo o detenido

    /**
     * Método principal que arranca el servidor, carga la configuración del
     * puerto, y maneja las excepciones que puedan surgir.
     *
     * @param args Argumentos de la línea de comandos (no utilizados)
     * @throws ErrorMaxClientes Si se supera el número máximo de clientes
     * permitidos
     */
    public static void main(String[] args) throws ErrorMaxClientes {
        cargarPuerto(); // Carga la configuración del puerto desde el archivo de propiedades
        try {
            System.out.println("Para cerrar el servidor, escriba 'cerrar' ");
            iniciar(); // Inicia el servidor
        } catch (IOException ex) {
            Logger.getLogger(AplicattionServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AplicattionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que inicia el servidor y se encarga de aceptar conexiones de
     * clientes. Además, arranca un hilo para escuchar comandos de consola (como
     * "cerrar").
     *
     * @throws IOException Si hay errores relacionados con los sockets
     * @throws SQLException Si ocurre un error con la base de datos
     * @throws ErrorMaxClientes Si se supera el número máximo de clientes
     * permitidos
     */
    private static void iniciar() throws IOException, SQLException, ErrorMaxClientes {

        // Iniciar un hilo para escuchar comandos como "cerrar" el servidor
        new Thread(AplicattionServer::escucharComandos).start();

        // Crear el ServerSocket en el puerto configurado
        serverSocket = new ServerSocket(puerto);
        DAO dao = (DAO) ServerFactory.getSignable(); // Obtener una instancia del DAO para la base de datos
        mensaje = new Mensaje(); // Instanciar el mensaje

        while (running) { // Mientras el servidor esté en ejecución
            try {
                // Esperando la conexión de un cliente
                LOGGER.info("Esperando conexión del cliente " + (numeroClientesConectados + 1));
                Socket socketCliente = serverSocket.accept(); // Aceptar la conexión de un cliente
                ObjectOutputStream salida = new ObjectOutputStream(socketCliente.getOutputStream()); // Crear flujo de salida

                LOGGER.info("Cliente " + (numeroClientesConectados + 1) + " conectado");

                // Verificar si se ha alcanzado el máximo de clientes
                if (numeroClientesConectados < getMaxCon()) {
                    // Crear y lanzar un hilo para gestionar la comunicación con el cliente
                    Worker worker = new Worker(socketCliente, salida, dao);
                    new Thread(worker).start();
                    numeroClientesConectados++; // Incrementar el contador de clientes conectados
                } else {
                    // Si se alcanza el máximo de clientes, enviar mensaje de error y cerrar la conexión
                    mensaje.setRq(Request.ERROR_MAX_CLIENTES);
                    salida.writeObject(mensaje);
                    socketCliente.close(); // Cerrar la conexión con el cliente
                }
            } catch (IOException e) {
                if (!running) {
                    LOGGER.info("El servidor se cerró y no se aceptan más conexiones.");
                } else {
                    LOGGER.severe("Error en el socket del servidor: ");
                }
            }
        }

        // Finalizar el servidor
        finalizar();
    }

    /**
     * Método que se ejecuta en un hilo separado para escuchar comandos desde la
     * consola. Permite al usuario escribir "cerrar" para detener el servidor.
     */
    private static void escucharComandos() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String comando = scanner.nextLine(); // Leer el comando del usuario
                if ("cerrar".equalsIgnoreCase(comando)) {
                    finalizar(); // Llamar al método para cerrar el servidor
                    break; // Salir del bucle
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error al escuchar comandos: " + e.getMessage());
        }
    }

    /**
     * Método que finaliza la ejecución del servidor, cerrando los sockets
     * abiertos.
     */
    public static void finalizar() {
        running = false; // Establecer la bandera running a false para detener el servidor
        try {
            // Cerrar el serverSocket si está abierto
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                LOGGER.info("Servidor cerrado correctamente.");
            }

            // Cerrar la conexión con el cliente si está abierta
            if (socketCliente != null) {
                socketCliente.close();
            }

        } catch (IOException e) {
            LOGGER.severe("Error al intentar cerrar algún socket: " + e.getMessage());
        }
    }

    /**
     * Obtiene el número máximo de clientes permitidos desde un archivo de
     * propiedades.
     *
     * @return El número máximo de clientes
     */
    private static int getMaxCon() {
        ResourceBundle bundle = ResourceBundle.getBundle("dataAccessTier.conexion"); // Cargar el archivo de configuración
        String recogerMaxCon = bundle.getString("maxCon"); // Obtener el valor de "maxCon"
        return Integer.valueOf(recogerMaxCon); // Devolver el número máximo de clientes
    }

    /**
     * Método sincronizado que decrementa el número de clientes conectados. Este
     * método se debe llamar cuando un cliente se desconecta.
     */
    public static synchronized void decrementarClientes() {
        numeroClientesConectados--; // Decrementar el número de clientes conectados
        LOGGER.info("Número de clientes conectados: " + numeroClientesConectados);
    }

    /**
     * Carga el número de puerto desde un archivo de propiedades y valida que
     * sea un número válido.
     *
     * @throws NumberFormatException Si el valor del puerto no es un número
     * válido
     */
    private static void cargarPuerto() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("libreria.puerto"); // Cargar el archivo de configuración del puerto
            String recogerPuerto = bundle.getString("PUERTO"); // Obtener el valor del puerto
            puerto = Integer.parseInt(recogerPuerto); // Parsear el puerto a un entero
        } catch (NumberFormatException e) {
            LOGGER.severe("El puerto en el archivo de propiedades no es un puerto válido: " + e.getMessage());
        }
    }
}
