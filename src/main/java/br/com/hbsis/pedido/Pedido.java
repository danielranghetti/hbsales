package br.com.hbsis.pedido;


import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.periodoVenda.PeriodoVenda;
import br.com.hbsis.produto.Produto;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "seg_pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "qtd_compra", length = 255)
    private int qtdCompra;
    @Column(name = "codigo_pedido")
    private String codPedido;
    @Column(name = "status",length = 10,nullable = false)
    private String status;
    @Column(name = "data", length = 15)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "id_produto", referencedColumnName = "id")
    private Produto produto;
    @ManyToOne
    @JoinColumn(name = "id_funcionario", referencedColumnName = "id")
    private Funcionario funcionario;
    @ManyToOne
    @JoinColumn(name = "id_periodo_venda",referencedColumnName = "id")
    private PeriodoVenda periodoVenda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQtdCompra() {
        return qtdCompra;
    }

    public void setQtdCompra(int qtdCompra) {
        this.qtdCompra = qtdCompra;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public PeriodoVenda getPeriodoVenda() {
        return periodoVenda;
    }

    public void setPeriodoVenda(PeriodoVenda periodoVenda) {
        this.periodoVenda = periodoVenda;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", qtdCompra=" + qtdCompra +
                ", codPedido='" + codPedido + '\'' +
                ", status='" + status + '\'' +
                ", data=" + data +
                ", produto=" + produto +
                ", funcionario=" + funcionario +
                ", periodoVenda=" + periodoVenda +
                '}';
    }
}

