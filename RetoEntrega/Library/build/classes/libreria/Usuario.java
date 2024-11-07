package libreria;

import java.io.Serializable;

/**
 * La clase `Usuario` representa a un usuario dentro del sistema.
 *
 * @author 2dam
 */
public class Usuario implements Serializable {

    private String email;     // Este será el nombre de usuario (email)
    private String password;  // Contraseña del usuario
    private String nombreyApellidos;    // Nombre y apellidos del usuario
    private String direccion;   // Dirección del usuario
    private String ciudad;      // Ciudad del usuario
    private int codigoPostal;   // Código postal del usuario
    private boolean estaActivo; // Estado de actividad del usuario (activo o no)

    /**
     * Constructor con todos los parámetros para crear un objeto `Usuario`.
     *
     * Este constructor inicializa todos los atributos de la clase con los
     * valores proporcionados.
     *
     * @param email El correo electrónico (nombre de usuario) del usuario.
     * @param password La contraseña del usuario.
     * @param nombreyApellidos El nombre y apellidos del usuario.
     * @param direccion La dirección del usuario.
     * @param ciudad La ciudad del usuario.
     * @param codigoPostal El código postal de la dirección del usuario.
     * @param estaActivo El estado de actividad del usuario (activo o no).
     */
    public Usuario(String email, String password, String nombreyApellidos, String direccion, String ciudad, int codigoPostal, boolean estaActivo) {
        this.email = email;
        this.password = password;
        this.nombreyApellidos = nombreyApellidos;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.estaActivo = estaActivo;
    }

    /**
     * Constructor con los parámetros mínimos (email y password) para crear un
     * objeto `Usuario`.
     *
     * Este constructor es útil cuando solo se necesitan las credenciales del
     * usuario para el proceso de autenticación.
     *
     * @param email El correo electrónico (nombre de usuario) del usuario.
     * @param password La contraseña del usuario.
     */
    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor por defecto. Crea un objeto `Usuario` sin inicializar los
     * atributos.
     */
    public Usuario() {
    }

    // Getters y Setters para acceder y modificar los atributos del usuario.
    /**
     * Obtiene el correo electrónico del usuario (nombre de usuario).
     *
     * @return El correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario (nombre de usuario).
     *
     * @param email El correo electrónico que se asignará al usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return La contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password La contraseña que se asignará al usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el nombre y apellidos del usuario.
     *
     * @return El nombre y apellidos del usuario.
     */
    public String getNombreyApellidos() {
        return nombreyApellidos;
    }

    /**
     * Establece el nombre y apellidos del usuario.
     *
     * @param nombreyApellidos El nombre y apellidos que se asignarán al
     * usuario.
     */
    public void setNombreyApellidos(String nombreyApellidos) {
        this.nombreyApellidos = nombreyApellidos;
    }

    /**
     * Obtiene la dirección del usuario.
     *
     * @return La dirección del usuario.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del usuario.
     *
     * @param direccion La dirección que se asignará al usuario.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la ciudad del usuario.
     *
     * @return La ciudad del usuario.
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad del usuario.
     *
     * @param ciudad La ciudad que se asignará al usuario.
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Obtiene el código postal de la dirección del usuario.
     *
     * @return El código postal del usuario.
     */
    public int getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Establece el código postal de la dirección del usuario.
     *
     * @param codigoPostal El código postal que se asignará al usuario.
     */
    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Obtiene el estado de actividad del usuario.
     *
     * @return `true` si el usuario está activo, `false` si no lo está.
     */
    public boolean isEstaActivo() {
        return estaActivo;
    }

    /**
     * Establece el estado de actividad del usuario.
     *
     * @param estaActivo El estado de actividad que se asignará al usuario
     * (`true` si está activo, `false` si no).
     */
    public void setEstaActivo(boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
}
