/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poo.pedidovenda.domain;

import entities.annotations.EntityDescriptor;
import entities.annotations.View;
import entities.annotations.Views;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author thiago
 */
@Entity
@Views(
        @View(name = "Produtos",
                title = "Produtos",
                namedQuery = "from Produto order by nome",
                filters = "nome",
                members = "nome,valorUnitario",
                template = "@CRUD+@PAGER+@FILTER"))
@EntityDescriptor(template = "@TABLE+@CRUD+@PAGER")
public class Produto implements Serializable {

    @Id
    @GeneratedValue
    private int codigo;

    @NotEmpty(message = "Informe o nome do produto")
    @Column(length = 40, nullable = false)
    private String nome;
    
    @Column(precision = 6, scale = 2)
    @NotNull(message = "Informe o valor unitario do produto")
    @Min(value = 0, message = "Valor unitario invalido")
    private Double valorUnitario;
    
    @Version
    private Integer versao;

    public Produto() {
        this.valorUnitario = 0.00;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
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
        hash = 37 * hash + this.codigo;
        hash = 37 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 37 * hash + (this.valorUnitario != null ? this.valorUnitario.hashCode() : 0);
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
        final Produto other = (Produto) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
            return false;
        }
        if (this.valorUnitario != other.valorUnitario && (this.valorUnitario == null || !this.valorUnitario.equals(other.valorUnitario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
