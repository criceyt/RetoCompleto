package libreria;

import excepciones.ErrorGeneral;
import libreria.Usuario;



/**
 *
 * @author 2dam
 */
public interface Signable {

    public Mensaje singUp(Mensaje mensaje);

    public Mensaje signIn(Mensaje mensaje) throws ErrorGeneral;
    
}
