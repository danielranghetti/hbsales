package br.com.hbsis.produto;

import java.time.LocalDate;

public class ProdutoDTO {

    private Long id;
    private String codProduto;
    private String nome;
    private double preco;
    private int uniCaixa;
    private double pesoUni;
    private LocalDate validade;
    private Long linhaCategoria;
    private  String unidadeMedida;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, String codProduto, String nome, double preco, int uniCaixa, double pesoUni, LocalDate validade, Long linhaCategoria, String unidadeMedida) {
        this.id = id;
        this.codProduto = codProduto;
        this.nome = nome;
        this.preco = preco;
        this.uniCaixa = uniCaixa;
        this.pesoUni = pesoUni;
        this.validade = validade;
        this.linhaCategoria = linhaCategoria;
        this.unidadeMedida = unidadeMedida;
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
        produto.getLinhaCategoria().getId(),
        produto.getUnidadeMedida()
        );
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
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
                ", id_linhaCategoria=" + linhaCategoria +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                '}';
    }
}
