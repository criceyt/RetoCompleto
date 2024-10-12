/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private Usuario usuario; // Objeto Usuario

    // Constructor
  public Message(Usuario usuario) {
        this.usuario = usuario;
    }



    public Usuario getUsuario() {
        return usuario;
    }
}
