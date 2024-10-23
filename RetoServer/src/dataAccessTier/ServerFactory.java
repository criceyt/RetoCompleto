/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccessTier;

import libreria.Signable;

/**
 *
 * @author oier, gorka
 */
public class ServerFactory {

    public static Signable getSignable() {
        return new DAO();
    }

}
