package br.com.hbsis.funcionario;

public class FuncionarioDTO {

    private  Long id;
    private String nomeFun;
    private  String eMail;
    private String uuid;

    public FuncionarioDTO() {
    }

    public FuncionarioDTO(Long id, String nomeFun, String eMail,String uuid) {
        this.id = id;
        this.nomeFun = nomeFun;
        this.eMail = eMail;
        this.uuid = uuid;
    }

    public static FuncionarioDTO of(Funcionario funcionario){
        return new FuncionarioDTO(
                funcionario.getId(),
                funcionario.getNomeFun(),
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

    public String getNomeFun() {
        return nomeFun;
    }

    public void setNomeFun(String nomeFun) {
        this.nomeFun = nomeFun;
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
                ", nomeFun='" + nomeFun + '\'' +
                ", eMail='" + eMail + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
