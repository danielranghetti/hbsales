package br.com.hbsis.pedido;


import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.periodoVenda.PeriodoVenda;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "seg_pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codigo_pedido")
    private String codPedido;
    @Column(name = "status",length = 10,nullable = false)
    private String status;
    @Column(name = "data", length = 15)
    private LocalDate data;


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
                ", codPedido='" + codPedido + '\'' +
                ", status='" + status + '\'' +
                ", data=" + data +
                ", funcionario=" + funcionario +
                ", periodoVenda=" + periodoVenda +
                '}';
    }
}

