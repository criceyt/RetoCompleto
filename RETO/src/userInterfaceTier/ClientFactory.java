package userInterfaceTier;

import libreria.Signable;


/**
 *
 * @author 2dam
 */
public class ClientFactory {
    
    public static Signable getSignable() throws Exception{
        
        Signable signable = new Client();
        
        return signable;
    
    }
}
