package dataAccessTier;

import java.sql.Connection;
import libreria.Signable;

public class FactoryDao {
    private ConnectionPool connectionPool; // Pool de conexiones

    public FactoryDao(ConnectionPool pool) {
        this.connectionPool = pool; // Asigna el ConnectionPool recibido
    }

    // Método para crear una nueva instancia de Dao
    public Signable createDao() {
        try {
            Connection connection = connectionPool.getConnection(); // Obtener una conexión del pool
            return new Dao(connection); // Pasar la conexión al Dao
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de excepciones
            throw new RuntimeException("Error al crear DAO", e); // Lanzar una excepción
        }
    }
}
