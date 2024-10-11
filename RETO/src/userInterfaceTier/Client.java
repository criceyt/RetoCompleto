package userInterfaceTier;

import libreria.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Usuario;

public class Client implements Signable {

    private final String HOST = "127.0.0.1";
    private final int PUERTO = 5000;

    public String iniciar(Usuario usuario, String action) {
        String response = "";
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream())) {

            System.out.println("Conexión realizada con el servidor");

            // Enviar la acción (registro o inicio de sesión) y el usuario al servidor
            salida.writeObject(action);
            salida.writeObject(usuario);

            // Esperar la respuesta del servidor
            response = (String) entrada.readObject(); // Suponiendo que el servidor envía un String como respuesta

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            response = "Error en la conexión: " + ex.getMessage();
        }
        return response; // Devolver la respuesta del servidor
    }

    @Override
    public void singUp(Usuario usuario) {
        String response = iniciar(usuario, "register");
        System.out.println(response); // Muestra la respuesta del servidor
    }

    @Override
    public boolean login(String username, String password) {
        Usuario usuario = new Usuario(username, password, null, null, null, null); 
        String response = iniciar(usuario, "login");
        System.out.println(response);
        return response.equals("Login exitoso"); 
    }
}
