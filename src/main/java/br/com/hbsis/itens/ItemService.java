package br.com.hbsis.itens;

import br.com.hbsis.pedido.ConexaoPedido;
import br.com.hbsis.produto.ConexaoProduto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {
    private   final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);
    private final ConexaoItem conexaoItem;
   private final ConexaoProduto conexaoProduto;
    private final ConexaoPedido conexaoPedido;

    @Autowired
    public ItemService(ConexaoItem conexaoItem, ConexaoProduto conexaoProduto, ConexaoPedido conexaoPedido) {
        this.conexaoItem = conexaoItem;
        this.conexaoProduto = conexaoProduto;
        this.conexaoPedido = conexaoPedido;

    }
    private void validate(ItemDTO itemDTO){
        LOGGER.info("Validando Item");
        if (itemDTO == null) {
            throw new IllegalArgumentException("ItemDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(itemDTO.getPedido().toString())){
            throw new IllegalArgumentException("Pedido do item não deve ser nulo");
        }
        if (StringUtils.isEmpty(itemDTO.getProduto().toString())){
            throw new IllegalArgumentException("Produto não deve ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(itemDTO.getQuantidade()))){
            throw  new IllegalArgumentException("Quantidade do pedido não deve ser nula");
        }

    }

    public ItemDTO save(ItemDTO itemDTO){
        this.validate(itemDTO);
        LOGGER.info("Salvando Item");
        LOGGER.debug("Item: {}", itemDTO);

        Item item = new Item();

        item.setQuantidade(itemDTO.getQuantidade());
        item.setProduto(conexaoProduto.findByProdutoId(itemDTO.getProduto()));
        item.setPedido(conexaoPedido.findByPedidoId(itemDTO.getPedido()));

        item = this.conexaoItem.save(item);
        return ItemDTO.of(item);
    }

    public  ItemDTO findById(Long id){
        Optional<Item> itemOptional = this.conexaoItem.findById(id);
        if (itemOptional.isPresent()){
            return ItemDTO.of(itemOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public ItemDTO update(ItemDTO itemDTO, Long id){
        Optional<Item> itemOptionalExistente = this.conexaoItem.findById(id);
        this.validate(itemDTO);
        if (itemOptionalExistente.isPresent()){
            Item itemExistente = itemOptionalExistente.get();
            LOGGER.info("Atualizando Item ... id:[{}]", itemExistente.getId());
            LOGGER.debug("Payload: {}", itemDTO);
            LOGGER.debug("Funcionário existente: {}", itemExistente);

            itemExistente.setQuantidade(itemDTO.getQuantidade());
            itemExistente.setProduto(conexaoProduto.findByProdutoId(itemDTO.getProduto()));
            itemExistente.setPedido(conexaoPedido.findByPedidoId(itemDTO.getPedido()));

            itemExistente = this.conexaoItem.save(itemExistente);
            return ItemDTO.of(itemExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public  void delete(Long id){
        LOGGER.info("Executando o delete para item de ID:[{}]",id);
        this.conexaoItem.deletePorId(id);
    }
}
