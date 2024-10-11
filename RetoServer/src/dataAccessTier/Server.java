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
            e.printStackTrace(); // Manejar el error según sea necesario
        }
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        String dbUrl;
        String dbUser;
        String dbPassword;
        int maxConnections;

        // Cargar propiedades desde el archivo
        try (InputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            dbUrl = properties.getProperty("db.url");
            dbUser = properties.getProperty("db.user");
            dbPassword = properties.getProperty("db.password");
            maxConnections = Integer.parseInt(properties.getProperty("max.connections"));
        } catch (IOException e) {
            e.printStackTrace();
            return; // Termina si no se pueden cargar las propiedades
        }

        Server server = new Server(dbUrl, dbUser, dbPassword, maxConnections);
        server.start();
    }
}
