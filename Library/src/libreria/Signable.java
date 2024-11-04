package libreria;

import exceptions.ErrorCorreoExistente;
import exceptions.ErrorGeneral;
import exceptions.ErrorMaxClientes;
import exceptions.ErrorUsuarioInexistente;
import exceptions.ErrorUsuarioNoActivo;



/**
 *
 * @author 2dam
 */
public interface Signable {

    public Usuario singUp(Mensaje mensaje) throws ErrorGeneral, ErrorCorreoExistente, ErrorMaxClientes;

    public Usuario signIn(Mensaje mensaje) throws ErrorGeneral, ErrorUsuarioNoActivo, ErrorUsuarioInexistente, ErrorMaxClientes;
    
}
