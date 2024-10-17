/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccessTier;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;


/**
 *
 * @author gorka
 */
public class Aplicattion {
    
    private final int PUERTO;
    private static final Logger LOGGER = Logger.getLogger(Aplicattion.class.getName());
    
    public Aplicattion(int PUERTO){
        this.PUERTO = PUERTO;
    }
    
    public static void main(String[] args) {
        Aplicattion server = new Aplicattion(5000);
        server.iniciar();
    }

    private void iniciar() {
        try {
            ServerSocket serverSocket = new ServerSocket(PUERTO);
            while(true){
                Socket socketCliente = serverSocket.accept();
                LOGGER.info("Cliente conectado");
                //Worker worker = ServerFactory.getInstance().hacerWorker(socketCliente);
                //new Thread(worker).start();
            }
        } catch (Exception e) {
           LOGGER.severe("Fallo al intentar crear el socket del servidor");
        }
    }
}
