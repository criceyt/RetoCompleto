package userInterfaceTier;

import libreria.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Message;

public class Client implements Signable {

    private final String HOST = "127.0.0.1";
    private final int PUERTO = 5000;

    public String iniciar(Message mensaje) {
        String response = "";
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream())) {

            System.out.println("Conexión realizada con el servidor");

            // Enviar el mensaje al servidor
            salida.writeObject(mensaje);

            // Esperar la respuesta del servidor
            response = (String) entrada.readObject(); // Suponiendo que el servidor envía un String como respuesta

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            response = "Error en la conexión: " + ex.getMessage();
        }
        return response; // Devolver la respuesta del servidor
    }

    @Override
    public boolean singUp(Message mensaje) {
        String response = iniciar(mensaje);
        System.out.println(response); // Muestra la respuesta del servidor
        return response.equals("Registro exitoso");
    }

    @Override
    public boolean login(Message mensaje) {
        String response = iniciar(mensaje);
        System.out.println(response);
        return response.equals("Login exitoso");
    }
}
