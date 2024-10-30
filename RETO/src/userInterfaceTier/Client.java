package userInterfaceTier;

import exceptions.ErroMaxClientes;
import exceptions.ErrorCorreoExistente;
import exceptions.ErrorGeneral;
import exceptions.ErrorUsuarioInexistente;
import exceptions.ErrorUsuarioNoActivo;
import libreria.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import libreria.Mensaje;
import libreria.Request;

/**
 *
 * @author gorka
 */
public class Client implements Signable {

    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    private final String HOST = "127.0.0.1";
    private static int puerto;
    Socket socket = null;
    ObjectInputStream entrada = null;
    ObjectOutputStream salida = null;
    Alert alert;

    // SING UP 
    @Override
    public Mensaje singUp(Mensaje mensaje) throws ErrorGeneral, ErrorCorreoExistente {

        try {
            // cargar pyuerto
            ResourceBundle bundle = ResourceBundle.getBundle("libreria.puerto");
            String recogerPuerto = bundle.getString("PUERTO");
            puerto = Integer.parseInt(recogerPuerto);

            // iniciar
            socket = new Socket(HOST, puerto);

            LOGGER.info("Conexion realizada con el servidor");

            entrada = new ObjectInputStream(socket.getInputStream());

            salida = new ObjectOutputStream(socket.getOutputStream());

            salida.writeObject(mensaje);
            mensaje = (Mensaje) entrada.readObject();
            System.out.println(mensaje.getRq());
            
            switch(mensaje.getRq()) {
                case ERROR_GENERAL:
                    throw new ErrorGeneral();
                case ERROR_USUARIO_YA_EXISTE:
                    throw new ErrorCorreoExistente();
                    
               
            }

        } catch (NumberFormatException e) {
            LOGGER.severe("El puerto en el archivo de propiedades, no es un puerto valido" + e.getMessage());
            puerto = 16700;
        } catch (IOException e) {
            LOGGER.severe("No se ha podido conectar con el servidor");
            throw new ErrorGeneral();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            finalizar();
        }

        return mensaje;
    }

    // SING IN 
    @Override
    public Mensaje signIn(Mensaje mensaje) throws ErrorGeneral, ErrorUsuarioNoActivo, ErrorUsuarioInexistente {

        try {
            // cargar pyuerto

            ResourceBundle bundle = ResourceBundle.getBundle("libreria.puerto");
            String recogerPuerto = bundle.getString("PUERTO");
            puerto = Integer.parseInt(recogerPuerto);

            // iniciar
            socket = new Socket(HOST, puerto);

            LOGGER.info("Conexion realizada con el servidor");

            entrada = new ObjectInputStream(socket.getInputStream());

            salida = new ObjectOutputStream(socket.getOutputStream());

            salida.writeObject(mensaje);
            mensaje = (Mensaje) entrada.readObject();
            System.out.println(mensaje.getRq());
            
            switch(mensaje.getRq()) {
                case SIGN_IN_EXITOSO:
                    SignController.abrirVista();
                    break;
                case ERROR_GENERAL:
                    throw new ErrorGeneral();
                    
                case USUARIO_NO_ACTIVO:
                    throw new ErrorUsuarioNoActivo();
                    
                case USUARIO_INEXISTENTE:
                    throw new ErrorUsuarioInexistente();
                
            }
            

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            throw new ErrorGeneral();
        } catch (NumberFormatException e) {
            LOGGER.severe("El puerto en el archivo de propiedades, no es un puerto valido" + e.getMessage());
            puerto = 16700;
        } catch (IOException e) {
            LOGGER.severe("No se ha podido conectar con el servidor");
            throw new ErrorGeneral();

        } finally {
            finalizar();
        }
        return mensaje;
    }

    // METODO FINALIZAR QUE COMPARTE
    private void finalizar() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
        } catch (IOException e) {
            LOGGER.severe("Error al intentar cerrar" + e.getMessage());
        }
    }
}
