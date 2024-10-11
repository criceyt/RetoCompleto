/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccessTier;

import libreria.Signable;

/**
 *
 * @author crice
 */
public class FactoryDao {

    private String url;
    private String user;
    private String password;

    public FactoryDao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    // Método para crear una nueva instancia de Dao
    public Signable createDao() {
        return new Dao(url, user, password);
    }
}
