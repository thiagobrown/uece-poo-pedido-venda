/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poo.pedidovenda.domain;

import entities.annotations.EntityDescriptor;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author thiago
 */
@Entity
@EntityDescriptor(hidden = true, pluralDisplayName="Itens do Pedido")
public class PedidoItem implements Serializable {

    @Id
    @GeneratedValue
    private Integer codigo;

    @ManyToOne(optional = false)
    @NotNull(message = "Informe o pedido")
    private Pedido pedido;

    @ManyToOne(optional = false)
    @NotNull(message = "Informe o produto")
    private Produto produto;

    @Min(value = 1, message = "Informe a quantidade")
    @Column(precision = 4)
    private Integer quantidade;
    
    @Column(precision = 6, scale = 2)
    private Double preco;
    
    @Transient
    private Double total;

    public PedidoItem() {
        this.quantidade = 1;
        this.preco = 0.00;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        this.preco = produto.getValorUnitario();
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public Double getTotal() {
        return quantidade * preco;
    }
    
    public void remover() {
        this.pedido.getItens().remove(this);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        hash = 29 * hash + (this.pedido != null ? this.pedido.hashCode() : 0);
        hash = 29 * hash + (this.produto != null ? this.produto.hashCode() : 0);
        hash = 29 * hash + (this.quantidade != null ? this.quantidade.hashCode() : 0);
        hash = 29 * hash + (this.preco != null ? this.preco.hashCode() : 0);
        hash = 29 * hash + (this.total != null ? this.total.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PedidoItem other = (PedidoItem) obj;
        if (this.codigo != other.codigo && (this.codigo == null || !this.codigo.equals(other.codigo))) {
            return false;
        }
        if (this.pedido != other.pedido && (this.pedido == null || !this.pedido.equals(other.pedido))) {
            return false;
        }
        if (this.produto != other.produto && (this.produto == null || !this.produto.equals(other.produto))) {
            return false;
        }
        if (this.quantidade != other.quantidade && (this.quantidade == null || !this.quantidade.equals(other.quantidade))) {
            return false;
        }
        if (this.preco != other.preco && (this.preco == null || !this.preco.equals(other.preco))) {
            return false;
        }
        if (this.total != other.total && (this.total == null || !this.total.equals(other.total))) {
            return false;
        }
        return true;
    }
}
