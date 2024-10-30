/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccessTier;

import exceptions.ErrorCorreoExistente;
import exceptions.ErrorGeneral;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import libreria.Mensaje;
import libreria.Request;
import libreria.Usuario;

/**
 *
 * @author 2dam
 */
public class Prueba {
    public static void main(String[] args) {
        Usuario usu = new Usuario("casa@gmail.com", "12345678Aa", "casa", "casacasa", "casasaca", 12123, true);
        Mensaje mensae = new Mensaje(usu, Request.SIGN_UP_REQUEST);
           
      
        try {
            DAO dao = new DAO();
            dao.singUp(mensae);
        } catch (ErrorCorreoExistente ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorGeneral ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
    
}
