package br.com.hbsis.produto;

import java.time.LocalDate;

public class ProdutoDTO {

    private Long id;
    private int codProduto;
    private String nome;
    private double preco;
    private int uniCaixa;
    private double pesoUni;
    private LocalDate validade;
    private Long linhaCategoria;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, int codProduto, String nome, double preco, int uniCaixa, double pesoUni, LocalDate validade, Long linhaCategoria) {
        this.id = id;
        this.codProduto = codProduto;
        this.nome = nome;
        this.preco = preco;
        this.uniCaixa = uniCaixa;
        this.pesoUni = pesoUni;
        this.validade = validade;
        this.linhaCategoria = linhaCategoria;
    }

       public static ProdutoDTO of(Produto produto){
        return new ProdutoDTO(
        produto.getId(),
        produto.getCodProduto(),
        produto.getNome(),
        produto.getPreco(),
        produto.getUniCaixa(),
        produto.getPesoUni(),
        produto.getValidade(),
        produto.getLinhaCategoria().getId()
        );
    }

    public Long getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(Long linhaCategoria) {
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

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    @Override
    public String toString() {
        return "ProdutoDTO{" +
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
