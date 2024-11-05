package userInterfaceTier;

import libreria.Signable;

/**
 * Fábrica de clientes que proporciona una instancia de {@link Signable}.
 * Esta clase encapsula la creación de objetos que implementan la 
 * interfaz {@link Signable}.
 * 
 * @author Ekain
 */
public class ClientFactory {
    
    /**
     * Obtiene una instancia de {@link Signable}.
     * 
     * @return una instancia de {@link Signable}.
     * @throws Exception si ocurre un error durante la creación del cliente.
     */
    public static Signable getSignable() throws Exception {
        
        Signable signable = new Client();
        
        return signable;
    
    }
}

