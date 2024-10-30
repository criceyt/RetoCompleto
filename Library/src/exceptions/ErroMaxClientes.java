/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author 2dam
 */
public class ErroMaxClientes extends Exception{
    public ErroMaxClientes() {
        super("Error Se ha alcanzado el maximo de clientes que se pueden conectar");
    }
}
