package dataAccessTier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Signable;
import libreria.Usuario;

/**
 *
 * @author gorka
 */
public class Worker implements Runnable {

    private Signable signable;
    private final int PUERTO = 5000;
    private final String clave = "abcd";

    public void iniciar(Usuario usuario) throws Exception {

        ServerSocket server = null;
        Socket socket = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;

        try {
            server = new ServerSocket(PUERTO);
            while (true) {
                socket = server.accept();
                System.out.println("Cliente conectado");

                salida = new ObjectOutputStream(socket.getOutputStream());
                entrada = new ObjectInputStream(socket.getInputStream());

                // Recibir el objeto Usuario
                usuario = (Usuario) entrada.readObject();

                salida.writeObject("Usuario recibido correctamente.");
            }

        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param usuario
     */
    @Override
    public void singUp(Usuario usuario) {
        try {
            iniciar(usuario);
        } catch (Exception ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void main(String[] args) {

    }

    @Override
    public void run() {

    }

}
