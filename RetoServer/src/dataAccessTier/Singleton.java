/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccessTier;

/**
 *
 * @author 2dam
 */
public class Singleton {

    private static PoolConexiones pool = null;

    private Singleton() {
    }

    public synchronized static PoolConexiones obtenerPool() {
        if (pool == null) {
            pool = new PoolConexiones();
        }
        return pool;
    }

}
