package br.com.hbsis.itens;

import br.com.hbsis.pedido.PedidoService;
import br.com.hbsis.produto.ConexaoProduto;
import br.com.hbsis.produto.ProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {
    private   final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);
    private final IItemRepository iItemRepository;
   private final ConexaoProduto conexaoProduto;
    private final PedidoService pedidoService;

    @Autowired
    public ItemService(IItemRepository iItemRepository, ConexaoProduto conexaoProduto, PedidoService pedidoService) {
        this.iItemRepository = iItemRepository;
        this.conexaoProduto = conexaoProduto;
        this.pedidoService = pedidoService;
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
        item.setPedido(pedidoService.findByPedidoId(itemDTO.getPedido()));

        item = this.iItemRepository.save(item);
        return ItemDTO.of(item);
    }

    public  ItemDTO findById(Long id){
        Optional<Item> itemOptional = this.iItemRepository.findById(id);
        if (itemOptional.isPresent()){
            return ItemDTO.of(itemOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public ItemDTO update(ItemDTO itemDTO, Long id){
        Optional<Item> itemOptionalExistente = this.iItemRepository.findById(id);
        this.validate(itemDTO);
        if (itemOptionalExistente.isPresent()){
            Item itemExistente = itemOptionalExistente.get();
            LOGGER.info("Atualizando Item ... id:[{}]", itemExistente.getId());
            LOGGER.debug("Payload: {}", itemDTO);
            LOGGER.debug("Funcionário existente: {}", itemExistente);

            itemExistente.setQuantidade(itemDTO.getQuantidade());
            itemExistente.setProduto(conexaoProduto.findByProdutoId(itemDTO.getProduto()));
            itemExistente.setPedido(pedidoService.findByPedidoId(itemDTO.getPedido()));

            itemExistente = this.iItemRepository.save(itemExistente);
            return ItemDTO.of(itemExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public  void delete(Long id){
        LOGGER.info("Executando o delete para item de ID:[{}]",id);
        this.iItemRepository.deleteById(id);
    }
}
