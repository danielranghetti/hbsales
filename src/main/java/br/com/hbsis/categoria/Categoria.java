package br.com.hbsis.categoria;


import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;


@Entity
@Table(name = "seg_categorias")
public  class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(name = "codigo_categoria", unique = true, nullable = false, length = 255)
    private String codigoCategoria;
    @Column(name = "nome_categoria", unique = true, nullable = false, length = 255)
    private String nomeCategoria;
    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id")
    private Fornecedor fornecedor;

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }


    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", codigoCategoria='" + codigoCategoria + '\'' +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                ", fornecedor=" + fornecedor +
                '}';
    }
}
