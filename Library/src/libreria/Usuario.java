package libreria;



import java.io.Serializable;

public class Usuario implements Serializable{
    private String email;     // Este será el nombre de usuario
    private String password;  // Contraseña
    private String nombreApellido;    // Nombre del usuario
    private String direccion;   // Apellido del usuario

    // Constructor
    public Usuario(String email, String password, String nombreApellido, String direccion) {
        this.email = email;
        this.password = password;
        this.nombreApellido = nombreApellido;
        this.direccion = direccion;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
