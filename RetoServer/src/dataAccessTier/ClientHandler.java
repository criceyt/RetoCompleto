package dataAccessTier;

import libreria.Message;
import libreria.Usuario;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

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

            System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());

            // Leer la acción y el mensaje
            String action = (String) entrada.readObject(); // Lee la acción
            Message mensaje = (Message) entrada.readObject(); // Lee el objeto Message

            if (action == null || mensaje == null) {
                System.err.println("Acción o mensaje nulo recibido");
                salida.writeObject("Error: Acción o mensaje nulo");
                salida.flush();
                return;
            }

            System.out.println("Acción recibida: " + action);
            System.out.println("Mensaje recibido: " + mensaje.getUsuario().getEmail());

            String response;
            switch (action) {
                case "register":
                    response = handleRegistration(mensaje);
                    break;
                case "login":
                    response = handleLogin(mensaje);
                    break;
                default:
                    response = "Acción desconocida";
                    break;
            }

            System.out.println("Enviando respuesta al cliente: " + response);
            salida.writeObject(response); // Envía la respuesta al cliente
            salida.flush(); // Asegúrate de que se envían todos los datos

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en el procesamiento del cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Cerrando el socket del cliente");
                clientSocket.close(); // Cierra el socket del cliente
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String handleRegistration(Message mensaje) {
        Usuario usuario = mensaje.getUsuario(); // Obtener el usuario del mensaje
        if (usuario == null || usuario.getEmail() == null) {
            return "Error: Usuario o email nulo";
        }

        System.out.println("Iniciando registro para: " + usuario.getEmail());

        Connection connection = null;
        try {
            connection = connectionPool.getConnection(); // Obtener conexión del pool
            if (connection == null) {
                throw new SQLException("No se pudo obtener una conexión de la pool");
            }
            Dao dao = new Dao(connection); // Crear una instancia del DAO
            System.out.println("Ejecutando registro en la base de datos...");
            boolean registered = dao.singUp(mensaje); // Usa el objeto Message para registro

            if (registered) {
                System.out.println("Registro exitoso para: " + usuario.getEmail());
                return "Registro exitoso";
            } else {
                System.out.println("Error en el registro: el usuario ya existe para: " + usuario.getEmail());
                return "Error en el registro: el usuario ya existe.";
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la conexión: " + e.getMessage());
            e.printStackTrace();
            return "Error al obtener la conexión.";
        } catch (Exception e) {
            System.err.println("Error durante el registro: " + e.getMessage());
            e.printStackTrace();
            return "Error durante el registro.";
        } finally {
            if (connection != null) {
                connectionPool.releaseConnection(connection); // Liberar la conexión
            }
        }
    }

    private String handleLogin(Message mensaje) {
        Usuario usuario = mensaje.getUsuario(); // Obtener el usuario del mensaje
        if (usuario == null || usuario.getEmail() == null) {
            return "Error: Usuario o email nulo";
        }

        Connection connection = null;
        try {
            connection = connectionPool.getConnection(); // Obtener una conexión del pool
            Dao dao = new Dao(connection); // Crear una instancia del DAO

            boolean authenticated = dao.login(mensaje); // Asegúrate de que `login` acepte un Message

            if (authenticated) {
                System.out.println("Login exitoso para: " + usuario.getEmail());
                return "Login exitoso";
            } else {
                System.out.println("Error en el inicio de sesión: credenciales incorrectas para: " + usuario.getEmail());
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
