package br.com.hbsis.periodoVenda;




import java.time.LocalDate;

public class PeriodoVendaDTO {

    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFinal;
    private LocalDate dataRetirada;
    private Long fornecedor;

    public PeriodoVendaDTO() {
    }

    public PeriodoVendaDTO(Long id, LocalDate dataInicio, LocalDate dataFinal, LocalDate dataRetirada, Long fornecedor) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
        this.dataRetirada = dataRetirada;
        this.fornecedor = fornecedor;
    }

    public static PeriodoVendaDTO of(PeriodoVenda periodoVenda) {
        return new PeriodoVendaDTO(
                periodoVenda.getId(),
                periodoVenda.getDataInicio(),
                periodoVenda.getDataFinal(),
                periodoVenda.getDataRetirada(),
                periodoVenda.getFornecedor().getId()
        );
    }

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

    public Long getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Long fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return "PeriodoVendaDTO{" +
                "id=" + id +
                ", dataInicio=" + dataInicio +
                ", dataFinal=" + dataFinal +
                ", dataRetirada=" + dataRetirada +
                ", fornecedor=" + fornecedor +
                '}';
    }
}