package br.com.hbsis.conexao;

import br.com.hbsis.itens.Item;
import br.com.hbsis.itens.ItemDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceItemDTO {

    private String itemName;
    private Integer amount;


    public InvoiceItemDTO() {
    }

    public InvoiceItemDTO(String itemName, Integer amount) {
        this.itemName = itemName;
        this.amount = amount;
    }
    public static List<InvoiceItemDTO> parserToList(List<Item> items) {
        return items.stream().map(item -> new InvoiceItemDTO(item.getProduto().getNome()
                , item.getQuantidade())).collect(Collectors.toList());
    }




    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "InvoiceItemDTO{" +
                "itemName='" + itemName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
