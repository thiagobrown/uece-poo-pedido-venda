/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poo.pedidovenda.domain;

import static entities.RepositoryAtomic.save;
import entities.annotations.ActionDescriptor;
import entities.annotations.EntityDescriptor;
import entities.annotations.ParameterDescriptor;
import entities.annotations.View;
import entities.annotations.Views;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotEmpty;
import poo.pedidovenda.domain.services.CreditoService;

/**
 *
 * @author thiago
 */
@Entity
//@Table(uniqueConstraints = {
//    @UniqueConstraint(columnNames = {"numero"})
//})
@EntityDescriptor(template = "@CRUD")
@Views({
    @View(title = "Registrar Pedido",
            name = "Pedidos",
            namedQuery = "Select new poo.pedidovenda.domain.Pedido()",
            members = "["
            + "#cliente:2;#numero,*dataPedido;"
            + "Itens[adicionarProduto();"
            + "         itens<*produto,*quantidade,*preco,*total,remover()>]:2;"
            + "aceitar():2]")
    ,    
  @View(title = "Lista de Pedidos",
            name = "ListaDePedidos",
            filters = "numero;cliente;dataPedido",
            members = "numero,cliente,dataPedido,valorTotal,status",
            template = "@FILTER+@PAGER",
            namedQuery = "from Pedido order by numero")
    ,
  @View(title = "Acompanhamento de Pedidos",
            name = "AcompanhamentoDePedidos",
            filters = "numero;cliente",
            members = "numero,cliente,dataPedido,valorTotal,'Ações'[pagar(),cancelar()]",
            template = "@FILTER+@PAGER",
            namedQuery = "from Pedido where status = poo.pedidovenda.domain.StatusEnum.Aceito order by numero")
})
public class Pedido implements Serializable {

    private static final Double LIMITE_PEDIDO = 1000.00;

    @Id
    @GeneratedValue
    private int codigo;

    @Column(length = 8, unique = true, nullable = false)
    @NotEmpty(message = "Informe o numero do pedido")
    private String numero;

    @ManyToOne(optional = false)
    private Cliente cliente;

    @NotNull(message = "O status do pedido não pode ser nulo!")
    @Enumerated(EnumType.STRING)
    protected StatusEnum status;

    @Valid
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<PedidoItem> itens;

    @Past(message = "A data deve está no passado")
    @Temporal(TemporalType.DATE)
    private Date dataPedido;

    @Formula("(select sum(pi.quantidade*pi.preco) from PedidoItem pi where pi.pedido_codigo = codigo)")
    @Column(precision = 8, scale = 2)
    private Double valorTotal;

    @Version
    private Integer versao;

    public Pedido() {
        this.status = StatusEnum.NovoPedido;
        this.dataPedido = new Date();
        this.itens = new ArrayList<PedidoItem>();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<PedidoItem> getItens() {
        return itens;
    }

    public void setItens(List<PedidoItem> itens) {
        this.itens = itens;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    @ActionDescriptor(refreshView = true)
    public String aceitar() {
        if (this.itens == null || this.itens.isEmpty()) {
            throw new IllegalStateException("Adicione pelo menos um produto ao pedido!");
        }
        
        if (!isOKLimitePedido()) {
            throw new IllegalStateException("Total do pedido não pode ser maior do que " + LIMITE_PEDIDO);
        }

        if (!isOKLimiteCliente()) {
            throw new IllegalStateException("Limite de crédito do Cliente excedido!");
        }

        this.status.aceitar(this);
        save(this);

        return "Pedido aceito!";
    }

    @ActionDescriptor(refreshView = true)
    public String pagar() {
        this.status.pagar(this);
        save(this);

        return "Pedido pago!";
    }

    @ActionDescriptor(refreshView = true)
    public String cancelar() {
        this.status.cancelar(this);
        save(this);

        return "Pedido cancelado!";
    }

    public void adicionarProduto(
            @ParameterDescriptor(displayName = "Produto") Produto produto,
            @ParameterDescriptor(displayName = "Quantidade") Integer quantidade) {

        PedidoItem item = new PedidoItem();
        item.setPedido(this);
        item.setProduto(produto);
        item.setQuantidade(quantidade);

        this.itens.add(item);
    }

    private Double valorAtualDoPedido() {
        Double total = 0.00;

        for (PedidoItem item : itens) {
            total += item.getTotal();
        }

        return total;
    }

    private boolean isOKLimitePedido() {
        return this.valorAtualDoPedido() <= LIMITE_PEDIDO;
    }

    private boolean isOKLimiteCliente() {
        if (this.cliente == null) {
            throw new IllegalArgumentException("Informe o cliente");
        }

        Double creditoCliente = CreditoService.getCreditoCliente(this.cliente);
        return creditoCliente + this.valorAtualDoPedido() <= this.cliente.getLimiteCredito();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.codigo;
        hash = 29 * hash + (this.numero != null ? this.numero.hashCode() : 0);
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
        final Pedido other = (Pedido) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        if ((this.numero == null) ? (other.numero != null) : !this.numero.equals(other.numero)) {
            return false;
        }
        return true;
    }

}
