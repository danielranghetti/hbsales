package br.com.hbsis.pedido;

import java.time.LocalDate;
import java.util.List;

public class PedidoDTO {

    private Long id;
    private Long Funcionario;
    private int qtdCompra;
    private Long produto;
    private LocalDate date;


    public PedidoDTO() {
    }

    public PedidoDTO(Long id, Long funcionario, int qtdCompra, Long produto, LocalDate date) {
        this.id = id;
        this.Funcionario = funcionario;
        this.qtdCompra = qtdCompra;
        this.produto = produto;
        this.date = date;
    }

    public static PedidoDTO of(Pedido pedido) {
        return new PedidoDTO(
                pedido.getId(),
                pedido.getFuncionario().getId(),
                pedido.getQtdCompra(),
                pedido.getProduto().getId(),
                pedido.getData()
        );


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getFuncionario() {
        return Funcionario;
    }

    public void setFuncionario(Long funcionario) {
        Funcionario = funcionario;
    }

    public int getQtdCompra() {
        return qtdCompra;
    }

    public void setQtdCompra(int qtdCompra) {
        this.qtdCompra = qtdCompra;
    }

    public Long getProduto() {
        return produto;
    }

    public void setProduto(Long produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", Funcionario=" + Funcionario +
                ", qtdCompra=" + qtdCompra +
                ", produto=" + produto +
                ", date=" + date +
                '}';
    }
}