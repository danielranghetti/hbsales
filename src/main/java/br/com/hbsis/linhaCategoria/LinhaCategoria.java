package br.com.hbsis.linhaCategoria;

import br.com.hbsis.categoria.Categoria;

import javax.persistence.*;

@Entity
@Table(name = "seg_linha_categorias")
public class LinhaCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_linha_categoria_", nullable = false, length = 255)
    private  String codLinhaCategoria;
    @Column(name = "nome_linha",nullable = false,length = 255)
    private String nomeLinha;
    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id")
    private Categoria categoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodLinhaCategoria() {
        return codLinhaCategoria;
    }

    public void setCodLinhaCategoria(String codLinhaCategoria) {
        this.codLinhaCategoria = codLinhaCategoria;
    }

    public String getNomeLinha() {
        return nomeLinha;
    }

    public void setNomeLinha(String nomeLinha) {
        this.nomeLinha = nomeLinha;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "linhaCategoria{" +
                "id=" + id +
                ", categoria=" + categoria +
                ", codLinhaCategoria=" + codLinhaCategoria +
                ", nomeLinha='" + nomeLinha + '\'' +
                '}';
    }
}
