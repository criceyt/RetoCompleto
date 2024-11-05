package libreria;

import java.io.Serializable;

/**
 * Clase que representa un mensaje que puede ser enviado o recibido entre 
 * el servidor y el cliente. Esta clase implementa {@link Serializable}, lo 
 * que permite que los objetos de tipo {@code Mensaje} puedan ser serializados 
 * y enviados a través de flujos de entrada y salida.
 * 
 * El objeto {@code Mensaje} contiene dos atributos principales:
 *  - Un usuario ({@code Usuario}) que se incluye en el mensaje.
 *  - Un tipo de solicitud o respuesta ({@code Request}) que indica qué acción 
 *    se está solicitando o cuál es el resultado de una operación.
 * 
 * Los objetos de tipo {@code Mensaje} se utilizan para transportar datos 
 * entre el cliente y el servidor.
 * 
 * @author oier
 */
public class Mensaje implements Serializable {

    // Atributos

    private Usuario user;
    private Request rq;

    // Constructores

    /**
     * Constructor que inicializa un mensaje con un usuario y una solicitud.
     * 
     * @param user El usuario asociado al mensaje.
     * @param rq La solicitud o respuesta relacionada con el mensaje.
     */
    public Mensaje(Usuario user, Request rq) {
        this.user = user;
        this.rq = rq;
    }

    /**
     * Constructor por defecto, que inicializa el mensaje sin valores.
     * Este constructor es útil para instanciar objetos vacíos que luego
     * serán configurados con los valores correspondientes.
     */
    public Mensaje() {
    }

    // Métodos getters y setters

    /**
     * Obtiene el usuario asociado a este mensaje.
     * 
     * @return El usuario asociado al mensaje.
     */
    public Usuario getUser() {
        return user;
    }

    /**
     * Establece el usuario asociado a este mensaje.
     * 
     * @param user El usuario que se desea asociar al mensaje.
     */
    public void setUser(Usuario user) {
        this.user = user;
    }

    /**
     * Obtiene la solicitud o respuesta asociada a este mensaje.
     * 
     * @return La solicitud o respuesta asociada al mensaje.
     */
    public Request getRq() {
        return rq;
    }

    /**
     * Establece la solicitud o respuesta asociada a este mensaje.
     * 
     * @param rq La solicitud o respuesta que se desea asociar al mensaje.
     */
    public void setRq(Request rq) {
        this.rq = rq;
    }
}

