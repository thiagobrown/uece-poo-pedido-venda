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
public class StatusNovoPedido implements IStatus {

    @Override
    public void aceitar(Pedido pedido) {
        pedido.status = StatusEnum.Aceito;
    }
    
    @Override
    public void pagar(Pedido pedido) {
        throw new IllegalStateException("O pedido ainda não foi aceito!");
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.status = StatusEnum.Cancelado;
    }
}