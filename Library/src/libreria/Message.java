package libreria;

import java.io.Serializable;

public class Message implements Serializable {
       private static final long serialVersionUID = 1L;
     private String action; // La acción a realizar (register, login)
    private Usuario usuario; // El objeto usuario

     public Message(Usuario usuario) {
        this.usuario = usuario;
    }


    public Message(String action, Usuario usuario) {
        this.action = action;
        this.usuario = usuario;
    }

    // Getters y Setters
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Message{" +
                "action='" + action + '\'' +
                ", usuario=" + usuario + // Esto llamará al toString() de Usuario
                '}';
    }
}