package br.com.hbsis.fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "seg_fornecedores")
class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "razao_social", unique = true, nullable = false, length = 255)
    private String razaoSocial;
    @Column(name = "cnpj", unique = true, nullable = false, length = 25)
    private String cnpj;
    @Column(name = "nome_fantasia", nullable = false, length = 255)
    private String nomeFantasia;
    @Column(name = "endereco", nullable = false, length = 255)
    private String endereco;
    @Column(name = "telefone", unique = true, nullable = false, length = 255)
    private String telefone;
    @Column(name = "e_mail", nullable = false, length = 255)
    private String eMail;


    public Long getId() {
        return id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "id=" + id +
                ", razao_social='" + razaoSocial + '\'' +
                ", cnpj=" + cnpj +
                ", nome_fantasia='" + nomeFantasia + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone=" + telefone +
                ", e_mail='" + eMail + '\'' +
                '}';
    }
}
