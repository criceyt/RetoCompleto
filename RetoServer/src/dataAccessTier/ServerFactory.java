/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccessTier;

import java.sql.SQLException;
import libreria.Signable;

/**
 *
 * @author oier, gorka
 */
public class ServerFactory {

    public static Signable getSignable() throws SQLException {
        return new DAO();
    }

}
