package dataAccessTier;

import java.sql.Connection;
import libreria.Signable;

/**
 *
 * @author crice
 */
public class FactoryDao {
    private ConnectionPool connectionPool; // Cambia a ConnectionPool

    public FactoryDao(ConnectionPool pool) {
        this.connectionPool = pool; // Asigna el ConnectionPool recibido
    }

    // Método para crear una nueva instancia de Dao
    public Signable createDao() {
        Connection connection = connectionPool.getConnection(); // Obtener una conexión del pool
        return new Dao(connection); // Pasar la conexión al Dao
    }
}
