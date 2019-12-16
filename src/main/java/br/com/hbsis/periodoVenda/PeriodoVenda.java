package br.com.hbsis.periodoVenda;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "seg_periodo_venda")
public class PeriodoVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_inicial", nullable = false, length = 255)
    private LocalDate dataInicio;
    @Column(name = "data_final", nullable = false, length = 255)
    private LocalDate dataFinal;
    @Column(name = "data_retirada", nullable = false, length = 255)
    private LocalDate dataRetirada;
    @Column (name = "descricao",length = 50)
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "id_fornecedor", referencedColumnName = "id")
    private Fornecedor fornecedor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "PeriodoVenda{" +
                "id=" + id +
                ", dataInicio=" + dataInicio +
                ", dataFinal=" + dataFinal +
                ", dataRetirada=" + dataRetirada +
                ", descricao='" + descricao + '\'' +
                ", fornecedor=" + fornecedor +
                '}';
    }
}



