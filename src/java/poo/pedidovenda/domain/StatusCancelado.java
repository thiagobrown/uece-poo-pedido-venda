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
public class StatusCancelado implements IStatus {

    @Override
    public void aceitar(Pedido pedido) {
        throw new IllegalStateException("O pedido está cancelado!");
    }

    @Override
    public void pagar(Pedido pedido) {
        throw new IllegalStateException("O pedido está cancelado!");
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new IllegalStateException("O pedido está cancelado!");
    }
    
}
