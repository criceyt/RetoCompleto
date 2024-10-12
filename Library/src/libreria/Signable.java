package libreria;

import libreria.Usuario;

/**
 *
 * @author 2dam
 */
import libreria.Message;

public interface Signable {

    public boolean login(Message mensaje);    // Cambiado para recibir un objeto Message

    public boolean singUp(Message mensaje);   // Cambiado para recibir un objeto Message
}
