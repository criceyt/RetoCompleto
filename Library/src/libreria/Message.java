package libreria;

import java.io.Serializable;

public class Message implements Serializable {
       private static final long serialVersionUID = 1L;
    private String action; // "login" o "register"
    private Usuario usuario;

    // Constructor para registro
    public Message(Usuario usuario) {
        this.usuario = usuario;
        this.action = (usuario != null) ? "register" : null; // Si el usuario no es nulo, es un registro
    }

    // Constructor para inicio de sesión
    public Message(String action, Usuario usuario) {
        this.action = action;
        this.usuario = usuario;
    }

    public String getAction() {
        return action;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
