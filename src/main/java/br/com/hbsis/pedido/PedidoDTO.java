package br.com.hbsis.pedido;



import br.com.hbsis.itens.Item;
import br.com.hbsis.itens.ItemDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {

    private Long id;
    private Long funcionario;
    private Long periodoVenda;
    private LocalDate date;
    private String status;
    private String codPedido;
    private List<ItemDTO> itemDTOList;

    public PedidoDTO(Long id, Long funcionario, Long periodoVenda, LocalDate date, String status, String codPedido, List<ItemDTO> itemDTOList) {
        this.id = id;
        this.funcionario = funcionario;
        this.periodoVenda = periodoVenda;
        this.date = date;
        this.status = status;
        this.codPedido = codPedido;
        this.itemDTOList = itemDTOList;
    }

    public static PedidoDTO of(Pedido pedido) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        pedido.getItemList().forEach(item -> itemDTOList.add(ItemDTO.of(item)));

        return new PedidoDTO(
                pedido.getId(),
                pedido.getFuncionario().getId(),
                pedido.getPeriodoVenda().getId(),
                pedido.getData(),
                pedido.getStatus(),
                pedido.getCodPedido(),
                itemDTOList
        );
    }


    public List<ItemDTO> getItemDTOList() {
        return itemDTOList;
    }

    public void setItemDTOList(List<ItemDTO> itemDTOList) {
        this.itemDTOList = itemDTOList;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
                ", date=" + date +
                ", status='" + status + '\'' +
                ", codPedido='" + codPedido + '\'' +
                ", itemDTOList=" + itemDTOList +
                '}';
    }


}