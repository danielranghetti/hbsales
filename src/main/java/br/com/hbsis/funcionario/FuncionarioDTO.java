package br.com.hbsis.funcionario;

public class FuncionarioDTO {

    private  Long id;
    private String nome;
    private  String eMail;
    private String uuid;

    public FuncionarioDTO() {
    }

    public FuncionarioDTO(Long id, String nome, String eMail, String uuid) {
        this.id = id;
        this.nome = nome;
        this.eMail = eMail;
        this.uuid = uuid;
    }

    public static FuncionarioDTO of(Funcionario funcionario){
        return new FuncionarioDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.geteMail(),
                funcionario.getUuid()
        );
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    @Override
    public String toString() {
        return "FuncionarioDTO{" +
                "id=" + id +
                ", nomeFun='" + nome + '\'' +
                ", eMail='" + eMail + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
