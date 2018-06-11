/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poo.pedidovenda.domain;

import entities.annotations.View;
import entities.annotations.Views;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CNPJ;

/**
 *
 * @author thiago
 */
@Entity
//@Table(uniqueConstraints = {
//    @UniqueConstraint(columnNames = {"numero"})
//})
@NamedQueries({
    @NamedQuery(name = "DebitoClienteQuery",
                query = "select sum(valorTotal) "
                        + " from Pedido"
                        + " where cliente = :cliente"
                        + " and status = poo.pedidovenda.domain.StatusEnum.Aceito")})
@Views(
        @View(name = "Clientes",
                title = "Clientes",
                namedQuery = "from Cliente order by nome",
                filters = "nome;cnpj;cidade",
                members = "[numero;nome;cnpj;cidade;limiteCredito;]",
                template = "@CRUD+@PAGER+@FILTER"))
public class Cliente implements Serializable {

    @Id
    @GeneratedValue
    private Integer codigo;

    @Column(length = 8, nullable = false, unique = true)
    @NotEmpty(message = "Informe o numero do cliente")
    private String numero;

    @NotEmpty(message = "Informe o nome do cliente")
    @Column(length = 40, nullable = false)
    private String nome;

    @CNPJ
    @NotEmpty(message = "Informe o CNPJ do cliente")
    @Column(length = 14)
    private String cnpj;

    @NotNull(message = "Informe a cidade do cliente")
    @ManyToOne(cascade = CascadeType.ALL)
    private Cidade cidade;

    @Min(0)
    @Column(precision = 8, scale = 2)
    private Double limiteCredito;

    @Version
    private Integer versao;

    public Cliente() {
        this.limiteCredito = 0.00;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(Double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        hash = 67 * hash + (this.numero != null ? this.numero.hashCode() : 0);
        hash = 67 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 67 * hash + (this.cnpj != null ? this.cnpj.hashCode() : 0);
        hash = 67 * hash + (this.cidade != null ? this.cidade.hashCode() : 0);
        hash = 67 * hash + (this.limiteCredito != null ? this.limiteCredito.hashCode() : 0);
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
        final Cliente other = (Cliente) obj;
        if ((this.numero == null) ? (other.numero != null) : !this.numero.equals(other.numero)) {
            return false;
        }
        if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
            return false;
        }
        if ((this.cnpj == null) ? (other.cnpj != null) : !this.cnpj.equals(other.cnpj)) {
            return false;
        }
        if (this.codigo != other.codigo && (this.codigo == null || !this.codigo.equals(other.codigo))) {
            return false;
        }
        if (this.cidade != other.cidade && (this.cidade == null || !this.cidade.equals(other.cidade))) {
            return false;
        }
        if (this.limiteCredito != other.limiteCredito && (this.limiteCredito == null || !this.limiteCredito.equals(other.limiteCredito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome + " [" + numero + "]";
    }
}
