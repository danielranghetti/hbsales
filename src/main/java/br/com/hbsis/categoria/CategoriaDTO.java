package br.com.hbsis.categoria;

public class CategoriaDTO {

    private Long id;
    private String codigoCategoria;
    private Long fornecedor;
    private String nomeCategoria;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Long id, String codigoCategoria, Long fornecedor, String nomeCategoria) {
        this.id = id;
        this.codigoCategoria = codigoCategoria;
        this.fornecedor = fornecedor;
        this.nomeCategoria = nomeCategoria;
    }

    public static CategoriaDTO of(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getCodigoCategoria(),
                categoria.getFornecedor().getId(),
                categoria.getNomeCategoria()
        );
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

    public Long getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Long fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", codigoCategoria=" + codigoCategoria +
                ", id_fornecedor=" + fornecedor +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                '}';
    }
}
