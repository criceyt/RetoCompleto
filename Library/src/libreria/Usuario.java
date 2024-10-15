package libreria;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email; // Este campo se usará como login
    private String password;
    private String nombreyApellidos;
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private String name; // Nombre completo para Odoo
    private int companyId; // company_id para Odoo
    private boolean active; // Indica si el usuario está activo

    public Usuario(String email, String password, String nombreyApellidos, String direccion, String ciudad, String codigoPostal) {
        this.email = email;
        this.password = password;
        this.nombreyApellidos = nombreyApellidos;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.name = nombreyApellidos; // Usar el nombre y apellidos como name
        this.companyId = 1; // Valor por defecto según tu implementación
        this.active = true; // Por defecto, el usuario está activo
    }

    @Override
    public String toString() {
        return "Usuario{"
                + "email='" + email + '\''
                + ", nombreyApellidos='" + nombreyApellidos + '\''
                + ", direccion='" + direccion + '\''
                + ", ciudad='" + ciudad + '\''
                + ", codigoPostal='" + codigoPostal + '\''
                + '}';
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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
