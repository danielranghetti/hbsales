package br.com.hbsis.funcionario;

import javax.persistence.*;

@Entity
@Table(name = "seg_funcionarios")
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", nullable = false, length = 255)
    private String nomeFun;
    @Column(name = "e_mail", nullable = false, length = 255)
    private String eMail;

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
        return "Funcionario{" +
                "id=" + id +
                ", nomeFun='" + nomeFun + '\'' +
                ", eMail='" + eMail + '\'' +
                '}';
    }
}