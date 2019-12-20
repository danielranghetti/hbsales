package br.com.hbsis.pedido;

import br.com.hbsis.ferramentas.Email;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.periodoVenda.PeriodoVenda;
import br.com.hbsis.periodoVenda.PeriodoVendaService;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class PedidoService {


    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);
    private final IPedidoRepository iPedidoRepository;
    private final FuncionarioService funcionarioService;
    private final PeriodoVendaService periodoVendaService;
    private final ProdutoService produtoService;
    private final Email email;

    public PedidoService(IPedidoRepository iPedidoRepository, FuncionarioService funcionarioService, PeriodoVendaService periodoVendaService, ProdutoService produtoService, Email email) {
        this.iPedidoRepository = iPedidoRepository;
        this.funcionarioService = funcionarioService;
        this.periodoVendaService = periodoVendaService;
        this.produtoService = produtoService;
        this.email = email;
    }

    public PedidoDTO save(PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();

        this.validate(pedidoDTO);

        pedido.setCodPedido(pedidoDTO.getCodPedido());
        pedido.setData(pedidoDTO.getDate());
        pedido.setId(pedidoDTO.getId());
        pedido.setFuncionario(funcionarioService.findByFuncionarioId(pedidoDTO.getFuncionario()));
        pedido.setProduto(produtoService.findByProdutoId(pedidoDTO.getProduto()));
        pedido.setPeriodoVenda(periodoVendaService.findByPeriodoVendaId(pedidoDTO.getPeriodoVenda()));
        pedido.setQtdCompra(pedidoDTO.getQtdCompra());
        pedido.setStatus(pedidoDTO.getStatus().toUpperCase());
        email.enviarEmailDataRetirada(pedido);

        pedido = this.iPedidoRepository.save(pedido);
        return PedidoDTO.of(pedido);
    }

    public PedidoDTO findByid(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (((Optional) pedidoOptional).isPresent()) {
            return PedidoDTO.of(pedidoOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validateUpdate(pedidoDTO, id);

        if (pedidoExistenteOptional.isPresent()) {
            Pedido pedidoExistente = pedidoExistenteOptional.get();

            LOGGER.info("Atualizando pedido... id:[{}]", pedidoExistente.getId());
            LOGGER.debug("Payloa: {}", pedidoDTO);
            LOGGER.debug("Pedido existente:{}", pedidoExistente);

            pedidoExistente.setData(LocalDate.now());
            pedidoExistente.setFuncionario(funcionarioService.findByFuncionarioId(pedidoDTO.getFuncionario()));
            pedidoExistente.setProduto(produtoService.findByProdutoId(pedidoDTO.getProduto()));
            pedidoExistente.setPeriodoVenda(periodoVendaService.findByPeriodoVendaId(pedidoDTO.getPeriodoVenda()));
            pedidoExistente.setQtdCompra(pedidoDTO.getQtdCompra());
            pedidoExistente.setStatus(pedidoDTO.getStatus().toUpperCase());

            pedidoExistente = this.iPedidoRepository.save(pedidoExistente);
            return PedidoDTO.of(pedidoExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para linha de categoria de ID:[{}]", id);
        this.iPedidoRepository.deleteById(id);
    }

    private void validate(PedidoDTO pedidoDTO) {
        LOGGER.info("Validando pedido");

        pedidoDTO.setCodPedido(gerandoCod());
        pedidoDTO.setDate(LocalDate.now());
        Produto produto;
        PeriodoVenda periodoVenda;
        while (iPedidoRepository.existsByCodPedido(pedidoDTO.getCodPedido())) {
            LOGGER.info("codigo de pedido já existente gerando um novo");
            pedidoDTO.setCodPedido(gerandoCod());
        }
        if (pedidoDTO == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getFuncionario()))) {
            throw new IllegalArgumentException("Funcionário não deve ser nulo");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getProduto()))) {
            throw new IllegalArgumentException("Produto não deve ser nulo");
        }
        produto = produtoService.findByProdutoId(pedidoDTO.getProduto());
        periodoVenda = periodoVendaService.findByPeriodoVendaId(pedidoDTO.getPeriodoVenda());
        if (produto.getLinhaCategoria().getCategoria().getFornecedor().getId() != periodoVenda.getFornecedor().getId()) {
            throw new IllegalArgumentException("produto não é do mesmo fornecedor que o periodo de venda");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getPeriodoVenda()))) {
            throw new IllegalArgumentException("Periodo de vendas não deve ser nulo");
        }
        if (iPedidoRepository.existsByCodPedido(pedidoDTO.getCodPedido())) {
            throw new IllegalArgumentException("O codigo do pedido já existe");
        }
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getQtdCompra()))) {
            throw new IllegalArgumentException("A quantidade não deve ser nula");
        }
        if (StringUtils.isEmpty(pedidoDTO.getStatus())) {
            throw new IllegalArgumentException("O status não deve ser nula");
        }

        switch (pedidoDTO.getStatus().toUpperCase()) {
            case "ATIVO":
            case "CANCELADO":
            case "RETIRADO":
                break;
            default:
                throw new IllegalArgumentException("Status do produto apenas pode ser: Ativo/Cancelado/Retirado");
        }
    }

    private void validateCan(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptionalPedido = this.iPedidoRepository.findById(id);
        pedidoDTO.setStatus("CANCELADO");
        pedidoDTO.setDate(LocalDate.now());

        if (pedidoExistenteOptionalPedido.isPresent()) {
            Pedido pedidoExistente = pedidoExistenteOptionalPedido.get();
            if (pedidoExistente.getPeriodoVenda().getDataFinal().isAfter(pedidoExistente.getPeriodoVenda().getDataFinal())) {
                throw new IllegalArgumentException("Periodo de vendas do pedido já terminou não podendo ser cancelado");
            }
            if (pedidoExistente.getStatus().equals("RETIRADO")) {
                throw new IllegalArgumentException("Já Retirado não podendo ser mas Cancelado");
            }
            if (pedidoExistente.getStatus().equals("CANCELADO")) {
                throw new IllegalArgumentException("Já cancelado");
            }
        }
    }

    private void validateUpdate(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptionalAltera = this.iPedidoRepository.findById(id);

        if (pedidoExistenteOptionalAltera.isPresent()) {
            Pedido pedidoExistente = pedidoExistenteOptionalAltera.get();
            if (pedidoExistente.getPeriodoVenda().getDataFinal().isAfter(pedidoExistente.getPeriodoVenda().getDataFinal())) {
                throw new IllegalArgumentException("Periodo de vendas do pedido já terminou não podendo ser alterado");
            }
            if (pedidoExistente.getStatus().equals("RETIRADO")) {
                throw new IllegalArgumentException("Já Retirado não podendo ser mas Alterado");
            }
            if (pedidoExistente.getStatus().equals("CANCELADO")) {
                throw new IllegalArgumentException("Já cancelado não podendo ser mais alterado");
            }
        }

    }

    private void validateRetira(PedidoDTO pedidoDTO, Long id) {

        Optional<Pedido> pedidoExistenteOptionalRetira = this.iPedidoRepository.findById(id);
        pedidoDTO.setStatus("RETIRADO");
        pedidoDTO.setDate(LocalDate.now());
        if (pedidoExistenteOptionalRetira.isPresent()) {
            Pedido pedidoExistenteRetira = pedidoExistenteOptionalRetira.get();
            if (pedidoExistenteRetira.getStatus().equals("CANCELADO")) {
                throw new IllegalArgumentException("Pedido foi cancelado ");
            }
            if (pedidoExistenteRetira.getStatus().equals("RETIRADO")) {
                throw new IllegalArgumentException("Pedido já Retirado");
            }
            if (pedidoExistenteRetira.getStatus().equals("ATIVO")) {
                LOGGER.info("Fazendo a retirada do Pedido ID... id: {}", id);
            }

        }
    }

    public String gerandoCod() {
        List codigos = new ArrayList();
        for (int i = 1; i < 61; i++) { //Sequencia da mega sena
            codigos.add(i);

        }
        Collections.shuffle(codigos);
        String codigoCompleto = StringUtils.leftPad(String.valueOf(codigos.get(0)), 10, "0");
        return codigoCompleto;
    }

    public List<PedidoDTO> findAllByFornecedorId(Long id) {
        Funcionario funcionario;
        funcionario = funcionarioService.findByFuncionarioId(id);

        List<Pedido> pedidos;
        pedidos = iPedidoRepository.findByFuncionario(funcionario);
        List<PedidoDTO> pedidoDTOList = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            try {
                if (pedido.getStatus().equals("ATIVO") || pedido.getStatus().equals("RETIRADO")) {
                    System.out.println(PedidoDTO.of(pedido));
                    pedidoDTOList.add(PedidoDTO.of(pedido));


                } else if (!(pedido.getStatus().equals("ATIVO") || pedido.getStatus().equals("RETIRADO"))) {
                    LOGGER.info("Pedido Cancelado");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return pedidoDTOList;
    }

    public PedidoDTO updateCancelar(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validateCan(pedidoDTO, id);


        if (pedidoExistenteOptional.isPresent()) {
            Pedido pedidoExistente = pedidoExistenteOptional.get();
            LOGGER.info("Atualizando pedido... id:[{}]", pedidoExistente.getId());
            LOGGER.info("Canelando pedido... id:[{}]", pedidoExistente.getId());
            LOGGER.debug("Payloa: {}", pedidoDTO);
            LOGGER.debug("Pedido existente:{}", pedidoExistente);


            pedidoExistente.setStatus(pedidoDTO.getStatus());
            pedidoExistente.setData(pedidoDTO.getDate());


            pedidoExistente = this.iPedidoRepository.save(pedidoExistente);
            return PedidoDTO.of(pedidoExistente);

        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public PedidoDTO updateRetira(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validateRetira(pedidoDTO, id);


        if (pedidoExistenteOptional.isPresent()) {
            Pedido pedidoExistenteRetira = pedidoExistenteOptional.get();
            LOGGER.info("Atualizando pedido... id:[{}]", pedidoExistenteRetira.getId());
            LOGGER.info("Retirando pedido... id:[{}]", pedidoExistenteRetira.getId());
            LOGGER.debug("Payloa: {}", pedidoDTO);
            LOGGER.debug("Pedido existente:{}", pedidoExistenteRetira);


            pedidoExistenteRetira.setStatus(pedidoDTO.getStatus());
            pedidoExistenteRetira.setData(pedidoDTO.getDate());


            pedidoExistenteRetira = this.iPedidoRepository.save(pedidoExistenteRetira);
            return PedidoDTO.of(pedidoExistenteRetira);

        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

}







