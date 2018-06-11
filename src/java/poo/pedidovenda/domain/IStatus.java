/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poo.pedidovenda.domain;

/**
 *
 * @author thiago
 */
public interface IStatus {

    void aceitar(Pedido pedido);

    void pagar(Pedido pedido);

    void cancelar(Pedido pedido);
}
