package br.com.hbsis.itens;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.produto.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConexaoItem {
    private final IItemRepository iItemRepository;

    @Autowired
    public ConexaoItem(IItemRepository iItemRepository) {
        this.iItemRepository = iItemRepository;
    }
    public List<Item> findByPedido(Pedido pedido){
        List<Item> itemList = new ArrayList<>();
        try {
            itemList = iItemRepository.findByPedido(pedido);
        }catch (Exception e){
            e.printStackTrace();
        }
        return itemList;
    }
    public void deletePorId(Long id){
        this.iItemRepository.deleteById(id);
    }

    public Item save(Item item){
        try {
            item = iItemRepository.save(item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return item;
    }
    public Optional<Item> findById(Long id){
        Optional<Item> itemOptional = iItemRepository.findById(id);
        if (itemOptional.isPresent()){
            return itemOptional;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe",id));
    }

    public  boolean existsProdutosPedidos(Produto produto, Pedido pedido){
        return iItemRepository.existsByProdutoAndPedido(produto,pedido);
    }
    public Item findByProdutosPedidos(Produto produto, Pedido pedido){
        Optional<Item> itemOptional = this.iItemRepository.findByProdutoAndPedido(produto,pedido);
        if (itemOptional.isPresent()){
            return itemOptional.get();
        }
        throw new IllegalArgumentException(String.format("pedido e produto %s não existe"));
    }
    public  boolean existsId(Long id){
        return iItemRepository.existsById(id);
    }



}
