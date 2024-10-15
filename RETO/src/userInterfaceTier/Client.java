package userInterfaceTier;

import java.io.EOFException;
import libreria.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Message;

public class Client implements Signable {

    private final String HOST = "127.0.0.1";  // IP del servidor
    private final int PUERTO = 5000;           // Puerto del servidor

    public String iniciar(Message mensaje) {
        String response = "";
        try (Socket socket = new Socket(HOST, PUERTO);
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Conexión realizada con el servidor");

            // Enviar el mensaje al servidor
            salida.writeObject(mensaje);
            salida.flush(); // Asegúrate de que los datos se envían correctamente

            // Esperar la respuesta del servidor
            response = (String) entrada.readObject();
            System.out.println("Respuesta del servidor: " + response);
        } catch (EOFException eof) {
            System.err.println("El servidor cerró la conexión antes de enviar una respuesta: " + eof.getMessage());
            response = "Error: El servidor cerró la conexión.";
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            response = "Error en la conexión: " + ex.getMessage();
        }
        return response;
    }

    @Override
    public boolean singUp(Message mensaje) {
        String response = iniciar(mensaje);
        System.out.println(response); // Muestra la respuesta del servidor

        // Informar que los datos fueron enviados
        if (response.equals("Registro exitoso")) {
            System.out.println("Los datos han sido enviados al servidor para registro.");
        } else {
            System.out.println("Error al enviar los datos al servidor.");
        }

        return response.equals("Registro exitoso");
    }

    @Override
    public boolean login(Message mensaje) {
        String response = iniciar(mensaje);
        System.out.println(response);

        // Informar que los datos fueron enviados
        if (response.equals("Login exitoso")) {
            System.out.println("Los datos de inicio de sesión han sido enviados al servidor.");
        } else {
            System.out.println("Error al enviar los datos de inicio de sesión al servidor.");
        }

        return response.equals("Login exitoso");
    }
}
