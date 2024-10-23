/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterfaceTier;

import java.io.FileInputStream;
import libreria.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import libreria.Usuario;

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

    private static void cargarPuerto() {
        Properties propiedades = new Properties();
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("libreria.puerto");
    

    String puertoStr = bundle.getString("PUERTO");
    puerto = Integer.parseInt(puertoStr);

        } catch (NumberFormatException e) {
            LOGGER.severe("El puerto en el archivo de propiedades, no es un puerto valido" + e.getMessage());
            puerto = 5600;
        }
    }

    public void iniciar(Usuario usuario) throws ClassNotFoundException {

        try {
            socket = new Socket(HOST, puerto);

            LOGGER.info("Conexion realizada con el servidor");

            entrada = new ObjectInputStream(socket.getInputStream());

            salida = new ObjectOutputStream(socket.getOutputStream());

            salida.writeObject(usuario);

        } catch (IOException e) {
            LOGGER.severe("No se ha podido conectar con el servidor" + e.getMessage());
        } finally {
            finalizar();
        }
    }

    @Override
    public void singUp(Usuario usuario) {
        try {
            cargarPuerto();
            iniciar(usuario);
        } catch (ClassNotFoundException e) {
            LOGGER.severe("Error en el registro de usuario" + e.getMessage());
        }
    }

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
            LOGGER.severe("Error al intentar cerrar"+ e.getMessage());
        }
    }

}
