package libreria;



import java.io.Serializable;

public class Usuario implements Serializable{
    private String email;     // Este será el nombre de usuario
    private String password;  // Contraseña
    private String nombreyApellidos;    // Nombre del usuario
    private String direccion;   // Apellido del usuario
    private String ciudad;
    private int codigoPostal;

    // Constructor
    public Usuario(String email, String password, String nombreyApellidos, String direccion, String ciudad, int codigoPostal) {
        this.email = email;
        this.password = password;
        this.nombreyApellidos = nombreyApellidos;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
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

    public String getNombreyApellidos() {
        return nombreyApellidos;
    }

    public void setNombreyApellidos(String nombreyApellidos) {
        this.nombreyApellidos = nombreyApellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getCiudad(){
        return ciudad;        
    }
    
    public void setCiudad(String ciudad){
        this.ciudad = ciudad;
    }
    
    public int getCodigoPostal(){
        return codigoPostal;
    }
    
    public void setCodigoPostal(int codigoPostal){
        this.codigoPostal = codigoPostal;
    }
}
