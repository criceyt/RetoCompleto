package libreria;

import exceptions.ErrorGeneral;
import exceptions.ErrorUsuarioInexistente;
import exceptions.ErrorUsuarioNoActivo;
import libreria.Usuario;



/**
 *
 * @author 2dam
 */
public interface Signable {

    public Mensaje singUp(Mensaje mensaje) throws ErrorGeneral;

    public Mensaje signIn(Mensaje mensaje) throws ErrorGeneral, ErrorUsuarioNoActivo, ErrorUsuarioInexistente;
    
}
