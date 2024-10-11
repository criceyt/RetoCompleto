package dataAccessTier;

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
             
            // Leer la acción
            String action = (String) entrada.readObject();
            Usuario usuario = (Usuario) entrada.readObject();

            // Procesar la acción
            String response;
            if ("register".equals(action)) {
                response = handleRegistration(usuario);
            } else if ("login".equals(action)) {
                response = handleLogin(usuario);
            } else {
                response = "Acción desconocida";
            }

            // Enviar la respuesta
            salida.writeObject(response);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el procesamiento del cliente: " + e.getMessage());
            e.printStackTrace(); // Manejo de errores
        } finally {
            try {
                clientSocket.close(); // Asegúrate de cerrar el socket
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

private String handleRegistration(Usuario usuario) {
    System.out.println("Iniciando registro para: " + usuario.getEmail());

    Connection connection = null;
    try {
        connection = connectionPool.getConnection();
        Dao dao = new Dao(connection);

        System.out.println("Llamando a singUp() para: " + usuario.getEmail());
        boolean registered = dao.singUp(usuario);
        
        if (registered) {
            System.out.println("Registro exitoso para: " + usuario.getEmail());
            return "Registro exitoso";
        } else {
            System.out.println("Error en el registro: el usuario ya existe para: " + usuario.getEmail());
            return "Error en el registro: el usuario ya existe.";
        }
    } catch (Exception e) {
        System.err.println("Error durante el registro: " + e.getMessage());
        e.printStackTrace();
        return "Error durante el registro.";
    } finally {
        if (connection != null) {
            connectionPool.releaseConnection(connection);
        }
    }
}


    private String handleLogin(Usuario usuario) {
        System.out.println("Iniciando sesión para: " + usuario.getEmail()); // Log de inicio de sesión

        // Obtener una conexión del ConnectionPool
        Connection connection = null;
        try {
            connection = connectionPool.getConnection(); // Obtener la conexión
            Dao dao = new Dao(connection); // Pasar la conexión al DAO

            boolean authenticated = dao.login(usuario.getEmail(), usuario.getPassword()); // Método en el DAO que maneja la autenticación

            if (authenticated) {
                System.out.println("Login exitoso para: " + usuario.getEmail()); // Log de éxito
                return "Login exitoso";
            } else {
                System.out.println("Error en el inicio de sesión: credenciales incorrectas para: " + usuario.getEmail()); // Log de error
                return "Error en el inicio de sesión: credenciales incorrectas.";
            }
        } catch (Exception e) {
            System.err.println("Error durante el inicio de sesión: " + e.getMessage());
            e.printStackTrace();
            return "Error durante el inicio de sesión.";
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection); // Liberar la conexión después de usarla
            }
        }
    }
}
