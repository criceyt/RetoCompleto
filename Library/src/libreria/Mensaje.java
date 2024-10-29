/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import java.io.Serializable;

/**
 *
 * @author 2dam
 */
public class Mensaje implements Serializable {
    
    // Atributos
    
    private Usuario user;
    private Request rq;
    
    // Constructores

    public Mensaje(Usuario user, Request rq) {
        this.user = user;
        this.rq = rq;
    }
    
    // getters and setters

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Request getRq() {
        return rq;
    }

    public void setRq(Request rq) {
        this.rq = rq;
    }
}
