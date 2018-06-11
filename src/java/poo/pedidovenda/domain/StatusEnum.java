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
public enum StatusEnum implements IStatus {

    NovoPedido(new StatusNovoPedido()),
    Aceito(new StatusAceito()),
    Pago(new StatusPago()),
    Cancelado(new StatusCancelado());

    private IStatus status;
    
    StatusEnum(IStatus status) {
        this.status = status;
    }
    
    @Override
    public void aceitar(Pedido pedido) {
        this.status.aceitar(pedido);
    }

    @Override
    public void pagar(Pedido pedido) {
        this.status.pagar(pedido);
    }

    @Override
    public void cancelar(Pedido pedido) {
        this.status.cancelar(pedido);
    }
}
