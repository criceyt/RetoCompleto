package dataAccessTier;

import java.sql.SQLException;
import libreria.Signable;

/**
 * Fábrica de objetos para crear instancias de clases que implementan la
 * interfaz {@link Signable}. Este patrón de diseño permite centralizar la
 * creación de objetos relacionados con la autenticación o la gestión de
 * usuarios, en este caso proporcionando una implementación concreta de
 * {@link Signable}.
 *
 * El método estático {@code getSignable()} se encarga de crear y devolver una
 * instancia de {@link DAO}, que es la implementación de la interfaz
 * {@link Signable}. Si ocurre un error de base de datos al crear la instancia
 * de {@code DAO}, el método lanza una excepción {@link SQLException}.
 *
 * @author oier, gorka
 */
public class ServerFactory {

    /**
     * Devuelve una instancia de un objeto que implementa la interfaz
     * {@link Signable}.
     *
     * @return Una instancia de {@link DAO} que implementa la interfaz
     * {@link Signable}.
     * @throws SQLException Si ocurre un error relacionado con la base de datos
     * al crear la instancia.
     */
    public static Signable getSignable() throws SQLException {
        return new DAO(); // Crear y devolver una nueva instancia de DAO.
    }
}
