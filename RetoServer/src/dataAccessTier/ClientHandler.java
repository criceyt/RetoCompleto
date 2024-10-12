package dataAccessTier;

import libreria.Message;
import libreria.Usuario;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ConnectionPool connectionPool;

    public ClientHandler(Socket socket, ConnectionPool pool) {
        this.clientSocket = socket;
        this.connectionPool = pool;
    }

    @Override
    public void run() {
        try (ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream salida = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String action = (String) entrada.readObject(); // Lee la acción (register o login)
            Usuario usuario = (Usuario) entrada.readObject(); // Lee el objeto Usuario

            // Crear el objeto Message con la acción y el usuario
            Message mensaje = new Message(action, usuario);

            String response;
            if ("register".equals(action)) {
                response = handleRegistration(mensaje); // Pasar el objeto Message
            } else if ("login".equals(action)) {
                response = handleLogin(mensaje); // Pasar el objeto Message
            } else {
                response = "Acción desconocida";
            }

            salida.writeObject(response); // Envía la respuesta al cliente
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Manejar el error según sea necesario
        } finally {
            try {
                clientSocket.close(); // Cierra el socket del cliente
            } catch (IOException e) {
                e.printStackTrace(); // Manejar el error según sea necesario
            }
        }
    }

    private String handleRegistration(Message mensaje) {
        // Obtener la conexión del ConnectionPool
        Connection connection = connectionPool.getConnection();
        Dao dao = new Dao(connection); // Pasar la conexión al DAO
        boolean registered = dao.singUp(mensaje); // Usar el objeto Message

        connectionPool.releaseConnection(connection); // Liberar la conexión después de usarla

        if (registered) {
            return "Registro exitoso";
        } else {
            return "Error en el registro: el usuario ya existe.";
        }
    }

    private String handleLogin(Message mensaje) {
        // Obtener la conexión del ConnectionPool
        Connection connection = connectionPool.getConnection();
        Dao dao = new Dao(connection); // Pasar la conexión al DAO
        boolean authenticated = dao.login(mensaje); // Usar el objeto Message

        connectionPool.releaseConnection(connection); // Liberar la conexión después de usarla

        if (authenticated) {
            return "Login exitoso";
        } else {
            return "Error en el inicio de sesión: credenciales incorrectas.";
        }
    }

}
