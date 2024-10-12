package userInterfaceTier;

import libreria.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Usuario;
import libreria.Message;

public class Client implements Signable {

    private final String HOST = "127.0.0.1";  // IP del servidor
    private final int PUERTO = 12345;         // Puerto

    private Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;

    public Client() {
        try {
            socket = new Socket(HOST, PUERTO);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Error al conectar con el servidor", ex);
        }
    }

    public String enviarSolicitud(String action, Message mensaje) {
        try {
            salida.writeObject(action);  // Enviar la acción (registro o login)
            salida.writeObject(mensaje);  // Enviar el mensaje
            salida.flush();  // Asegurarse de que los datos se envían correctamente
            return (String) entrada.readObject();  // Esperar la respuesta del servidor
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return "Error en la conexión: " + ex.getMessage();
        }
    }

    public void close() {
        try {
            socket.close();  // Cerrar la conexión cuando termines
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void registrarUsuario(String email, String password, String nombreyApellidos, String direccion, String ciudad, String codigoPostal) {
        Usuario usuario = new Usuario(email, password, nombreyApellidos, direccion, ciudad, codigoPostal);
        Message mensaje = new Message(usuario);
        boolean registrado = singUp(mensaje);

        if (registrado) {
            System.out.println("Registro exitoso para: " + email);
        } else {
            System.out.println("Error en el registro para: " + email);
        }
    }

    @Override
    public boolean login(Message mensaje) {
        System.out.println("Iniciando sesión...");
        String response = enviarSolicitud("login", mensaje);  // Enviar solicitud de login
        System.out.println(response);
        return response.equals("Login exitoso");
    }

    @Override
    public boolean singUp(Message mensaje) {  // Cambié el parámetro a Message
        System.out.println("Iniciando registro...");
        String response = enviarSolicitud("register", mensaje);  // Enviar solicitud de registro
        System.out.println(response);
        return response.equals("Registro exitoso");
    }

   
}
