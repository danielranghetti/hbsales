package br.com.hbsis.linhaCategoria;

public class LinhaCategoriaDTO {
    private Long id;
    private  Long codLinhaCategoria;
    private Long categoria;
    private String nomeLinha;

    public LinhaCategoriaDTO() {
    }

    public LinhaCategoriaDTO(Long id, Long codLinhaCategoria, Long categoria, String nomeLinha) {
        this.id = id;
        this.codLinhaCategoria = codLinhaCategoria;
        this.categoria = categoria;
        this.nomeLinha = nomeLinha;
    }

    public static LinhaCategoriaDTO of(LinhaCategoria linhaCategoria){
        return new LinhaCategoriaDTO(
                linhaCategoria.getId(),
                linhaCategoria.getCategoria().getId(),
                linhaCategoria.getCodLinhaCategoria(),
                linhaCategoria.getNomeLinha()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodLinhaCategoria() {
        return codLinhaCategoria;
    }

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    public void setCodLinhaCategoria(Long codLinhaCategoria) {
        this.codLinhaCategoria = codLinhaCategoria;
    }

    public String getNomeLinha() {
        return nomeLinha;
    }

    public void setNomeLinha(String nomeLinha) {
        this.nomeLinha = nomeLinha;
    }

    @Override
    public String toString() {
        return "LinhaCategoriaDTO{" +
                "id=" + id +
                ", id_categoria=" + categoria +
                ", codLinhaCategoria=" + codLinhaCategoria +
                ", nomeLinha='" + nomeLinha + '\'' +
                '}';
    }
}
