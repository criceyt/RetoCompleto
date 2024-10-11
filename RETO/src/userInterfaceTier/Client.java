/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterfaceTier;

import libreria.Signable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Usuario;

/**
 *
 * @author 2dam
 */
public class Client implements Signable {

    private final String HOST = "127.0.0.1";
    private final int PUERTO = 5000;

    public void iniciar(Usuario usuario) throws ClassNotFoundException {
        Socket socket = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;

        try {
            socket = new Socket(HOST, PUERTO);
            entrada = new ObjectInputStream(socket.getInputStream());
            salida = new ObjectOutputStream(socket.getOutputStream());
            
            
            System.out.println("Conexión realizada con servidor");
            
            salida.writeObject(usuario);
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void singUp(Usuario usuario) {
      try {
            iniciar(usuario);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



}
