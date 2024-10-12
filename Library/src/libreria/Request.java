/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import java.io.Serializable;

/**
 *
 * @author crice
 */
public enum Request implements Serializable{
    SING_UP_REQUEST,  // Registro de cuenta
    SING_IN_REQUEST,  // Inicio de sesión
    SING_OUT_REQUEST, // Cierre de sesión
    CLOSE,     // Apagar servidor
    INTERNAL_EXCEPTION,
    LOG_IN_EXCEPTION,
    CONNECTIONS_EXCEPTION,
    USER_EXISTS_EXCEPTION
}
