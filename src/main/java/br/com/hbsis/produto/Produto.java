package br.com.hbsis.produto;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.linhaCategoria.LinhaCategoria;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "seg_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_produto", nullable = false)
    private int codProduto;
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;
    @Column(name = "preco", nullable = false)
    private  double preco;
    @Column(name = "uni_caixa", nullable = false)
    private int uniCaixa;
    @Column(name = "peso_uni", nullable = false, length = 255)
    private double pesoUni;
    @Column(name = "validade", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date validade;
    @ManyToOne
    @JoinColumn(name = "id_linha_categoria", referencedColumnName = "id")
    private LinhaCategoria linhaCategoria;

    public LinhaCategoria getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(LinhaCategoria linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(int codProduto) {
        this.codProduto = codProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getUniCaixa() {
        return uniCaixa;
    }

    public void setUniCaixa(int uniCaixa) {
        this.uniCaixa = uniCaixa;
    }

    public double getPesoUni() {
        return pesoUni;
    }

    public void setPesoUni(double pesoUni) {
        this.pesoUni = pesoUni;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codProduto=" + codProduto +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", uniCaixa=" + uniCaixa +
                ", pesoUni=" + pesoUni +
                ", validade=" + validade +
                ", id_linha_categoria=" + linhaCategoria +
                '}';
    }
}
