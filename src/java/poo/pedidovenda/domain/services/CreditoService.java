/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poo.pedidovenda.domain.services;

import entities.Repository;
import poo.pedidovenda.domain.Cliente;

/**
 *
 * @author thiago
 */
public class CreditoService {
    
    public static Double getCreditoCliente(Cliente cliente) {
        Double credito;
        
        credito = (Double) Repository.query("DebitoClienteQuery", cliente).get(0);
        
        if (credito == null) {
            credito  = 0.00;
        }
        
        return credito;
    }  
}
