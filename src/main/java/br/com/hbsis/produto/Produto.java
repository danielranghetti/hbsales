package br.com.hbsis.produto;

import br.com.hbsis.linhaCategoria.LinhaCategoria;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "seg_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_produto", nullable = false)
    private String codProduto;
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;
    @Column(name = "preco", nullable = false)
    private  double preco;
    @Column(name = "uni_caixa", nullable = false)
    private int uniCaixa;
    @Column(name = "peso_uni", nullable = false)
    private double pesoUni;
    @Column(name = "validade", nullable = false)
    private LocalDate validade;
    @Column (name = "unidade_medida" ,length = 2)
    private String unidadeMedida;

    @ManyToOne
    @JoinColumn(name = "id_linha_categoria", referencedColumnName = "id")
    private LinhaCategoria linhaCategoria;

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setLinhaCategoria(LinhaCategoria linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
    }

    public LinhaCategoria getLinhaCategoria() {
        return linhaCategoria;
    }

     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(String codProduto) {
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

    public LocalDate getValidade(LocalDate parse) {
        return validade;
    }

    public void setValidade(LocalDate validade) {
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
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", id_linhaCategoria=" + linhaCategoria +
                '}';
    }


}
