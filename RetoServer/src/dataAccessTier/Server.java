package dataAccessTier;

import libreria.Usuario;
import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 5000; // Puerto para el servidor
    private ConnectionPool connectionPool;
    private ExecutorService executorService;

    public Server(String dbUrl, String dbUser, String dbPassword, int maxConnections) {
        connectionPool = new ConnectionPool(dbUrl, dbUser, dbPassword, maxConnections);
        executorService = Executors.newFixedThreadPool(maxConnections); // Crea un pool de hilos
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(new ClientHandler(clientSocket, connectionPool));
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace(); // Manejar el error según sea necesario
        }
    }

    public static void main(String[] args) {
        Properties properties = new Properties();

        // Cargar el archivo de propiedades desde el classpath
        try (InputStream input = Server.class.getClassLoader().getResourceAsStream("dataAccessTier/config.properties")) {
            if (input == null) {
                System.out.println("Lo siento, no se encontró el archivo config.properties en el classpath.");
                return;
            }
            properties.load(input);

            String dbUrl = properties.getProperty("db.url");
            String dbUser = properties.getProperty("db.user");
            String dbPassword = properties.getProperty("db.password");
            int maxConnections = Integer.parseInt(properties.getProperty("max.connections"));

            // Usar estos valores en tu aplicación
            System.out.println("URL de la base de datos: " + dbUrl);
            System.out.println("Usuario de la base de datos: " + dbUser);
            System.out.println("Máximo de conexiones: " + maxConnections);

            // Iniciar el servidor con los parámetros obtenidos
            Server server = new Server(dbUrl, dbUser, dbPassword, maxConnections);
            server.start(); // Iniciar el servidor

        } catch (FileNotFoundException e) {
            System.out.println("El archivo config.properties no fue encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo config.properties: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error al parsear el número de conexiones: " + e.getMessage());
        } catch (Exception ex) {
            System.err.println("Error inesperado: " + ex.getMessage());
            ex.printStackTrace(); // Para cualquier otro error
        }
    }
}
