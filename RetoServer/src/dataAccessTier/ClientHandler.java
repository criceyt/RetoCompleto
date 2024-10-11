/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccessTier;

import libreria.Usuario;
import java.io.*;
import java.net.Socket;

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

            String response;
            if ("register".equals(action)) {
                response = handleRegistration(usuario);
            } else if ("login".equals(action)) {
                response = handleLogin(usuario);
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

    private String handleRegistration(Usuario usuario) {
        // Lógica para registrar el usuario en la base de datos usando el DAO
        Dao dao = new Dao(connectionPool);
        boolean registered = dao.singUp(usuario); // Método en el DAO que maneja el registro

        if (registered) {
            return "Registro exitoso";
        } else {
            return "Error en el registro: el usuario ya existe.";
        }
    }

    private String handleLogin(Usuario usuario) {
        // Lógica para autenticar el usuario en la base de datos usando el DAO
        Dao dao = new Dao(connectionPool);
        boolean authenticated = dao.login(usuario.getEmail(), usuario.getPassword()); // Método en el DAO que maneja la autenticación

        if (authenticated) {
            return "Login exitoso";
        } else {
            return "Error en el inicio de sesión: credenciales incorrectas.";
        }
    }
}

