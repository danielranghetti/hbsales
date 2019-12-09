package br.com.hbsis.pedido;

import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.periodoVenda.PeriodoVendaService;
import br.com.hbsis.produto.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PedidoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);

    private final IPedidoRepository iPedidoRepository;
    private final FuncionarioService funcionarioService;

    private  final PeriodoVendaService periodoVendaService;
    private  final ProdutoService produtoService;


    public PedidoService(IPedidoRepository iPedidoRepository, FuncionarioService funcionarioService, PeriodoVendaService periodoVendaService, ProdutoService produtoService) {
        this.iPedidoRepository = iPedidoRepository;
        this.funcionarioService = funcionarioService;
        this.periodoVendaService = periodoVendaService;
        this.produtoService = produtoService;
    }
    public PedidoDTO save(PedidoDTO pedidoDTO){

        Pedido pedido = new Pedido();

        pedido.setData(pedidoDTO.getDate());
        pedido.setFuncionario(funcionarioService.findByFuncionarioId(pedidoDTO.getFuncionario()));
        pedido.setProduto(produtoService.findByProdutoId(pedidoDTO.getProduto()));
        pedido.setId(pedidoDTO.getId());
        pedido.setQtdCompra(pedidoDTO.getQtdCompra());

        pedido = this.iPedidoRepository.save(pedido);
        return  PedidoDTO.of(pedido);

    }
    public PedidoDTO findByid(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (((Optional) pedidoOptional).isPresent()) {
            return PedidoDTO.of(pedidoOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public Pedido findByLinhaCategoriaId(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (pedidoOptional.isPresent()) {
            return pedidoOptional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public PedidoDTO update(PedidoDTO pedidoDTO, Long id){
        Optional<Pedido> pedidoExistenteOptional = this.iPedidoRepository.findById(id);

        if (pedidoExistenteOptional.isPresent()){
            Pedido pedidoExistente = pedidoExistenteOptional.get();

            LOGGER.info("Atualizando pedido... id:[{}]",pedidoExistente.getId());
            LOGGER.debug("Payloa: {}", pedidoDTO);
            LOGGER.debug("Pedido existente:{}", pedidoExistente);

            pedidoExistente.setData(pedidoDTO.getDate());
            pedidoExistente.setFuncionario(funcionarioService.findByFuncionarioId(pedidoDTO.getFuncionario()));
            pedidoExistente.setProduto(produtoService.findByProdutoId(pedidoDTO.getProduto()));
            pedidoExistente.setId(pedidoDTO.getId());
            pedidoExistente.setQtdCompra(pedidoDTO.getQtdCompra());

            pedidoExistente = this.iPedidoRepository.save(pedidoExistente);
            return  PedidoDTO.of(pedidoExistente);

        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
    public void delete(Long id) {
        LOGGER.info("Executando delete para linha de categoria de ID:[{}]", id);
        this.iPedidoRepository.deleteById(id);
    }


}
