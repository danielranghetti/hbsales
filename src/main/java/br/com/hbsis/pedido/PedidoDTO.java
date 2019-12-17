package br.com.hbsis.pedido;

import java.time.LocalDate;
import java.util.List;

public class PedidoDTO {

    private Long id;
    private Long funcionario;
    private Long periodoVenda;
    private Long produto;
    private LocalDate date;
    private int qtdCompra;
    private String status;
    private String codPedido;


    public PedidoDTO() {
    }

    public PedidoDTO(Long id, Long funcionario, Long periodoVenda, Long produto, LocalDate date, int qtdCompra, String status, String codPedido) {
        this.id = id;
        this.funcionario = funcionario;
        this.periodoVenda = periodoVenda;
        this.produto = produto;
        this.date = date;
        this.qtdCompra = qtdCompra;
        this.status = status;
        this.codPedido = codPedido;
    }
    public static PedidoDTO of(Pedido pedido) {
        return new PedidoDTO(
               pedido.getId(),
                pedido.getFuncionario().getId(),
                pedido.getPeriodoVenda().getId(),
                pedido.getProduto().getId(),
                pedido.getData(),
                pedido.getQtdCompra(),
                pedido.getStatus(),
                pedido.getCodPedido()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Long funcionario) {
        this.funcionario = funcionario;
    }

    public Long getPeriodoVenda() {
        return periodoVenda;
    }

    public void setPeriodoVenda(Long periodoVenda) {
        this.periodoVenda = periodoVenda;
    }

    public Long getProduto() {
        return produto;
    }

    public void setProduto(Long produto) {
        this.produto = produto;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getQtdCompra() {
        return qtdCompra;
    }

    public void setQtdCompra(int qtdCompra) {
        this.qtdCompra = qtdCompra;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", funcionario=" + funcionario +
                ", periodoVenda=" + periodoVenda +
                ", produto=" + produto +
                ", date=" + date +
                ", qtdCompra=" + qtdCompra +
                ", status='" + status + '\'' +
                ", codPedido='" + codPedido + '\'' +
                '}';
    }
}