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
    private Long codigoCategoria;
    @Column(name = "nome_categoria", unique = true, nullable = false, length = 255)
    private String nomeCategoria;
    @ManyToOne
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;





    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }


    public Long getId() {
        return id;
    }

    public Long getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(Long codigoCategoria) {
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
                ", id_fornecedor=" + fornecedor +
                ", codigoCategoria=" + codigoCategoria +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                '}';
    }
}
