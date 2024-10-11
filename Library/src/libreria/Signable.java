package libreria;

import libreria.Usuario;

/**
 *
 * @author 2dam
 */
public interface Signable {

    public boolean login(String username, String password);

    boolean singUp(Usuario usuario);

}
