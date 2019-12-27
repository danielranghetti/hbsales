package br.com.hbsis.pedido;

import br.com.hbsis.conexao.InvoiceDTO;
import br.com.hbsis.ferramentas.Email;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.itens.Item;
import br.com.hbsis.itens.ItemDTO;
import br.com.hbsis.itens.ItemService;
import br.com.hbsis.periodoVenda.PeriodoVendaService;
import br.com.hbsis.produto.IProdutoRepository;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.nio.cs.ext.IBM300;

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
    private final ItemService itemService;
    private final ProdutoService produtoService;


    private final Email email;

    public PedidoService(IPedidoRepository iPedidoRepository, FuncionarioService funcionarioService, PeriodoVendaService periodoVendaService, @Lazy ItemService itemService, ProdutoService produtoService, Email email) {
        this.iPedidoRepository = iPedidoRepository;
        this.funcionarioService = funcionarioService;
        this.periodoVendaService = periodoVendaService;
        this.itemService = itemService;
        this.produtoService = produtoService;
        this.email = email;
    }

    public PedidoDTO save(PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();
        List<Item> itemList = new ArrayList<>();


        this.validate(pedidoDTO);

        pedido.setCodPedido(pedidoDTO.getCodPedido());
        pedido.setData(pedidoDTO.getDate());
        pedido.setId(pedidoDTO.getId());
        pedido.setFuncionario(funcionarioService.findByFuncionarioId(pedidoDTO.getFuncionario()));
        pedido.setPeriodoVenda(periodoVendaService.findByPeriodoVendaId(pedidoDTO.getPeriodoVenda()));
        pedido.setStatus(pedidoDTO.getStatus().toUpperCase());


        if (invoiceValidarPedido(pedido.getPeriodoVenda().getFornecedor().getCnpj(), pedido.getFuncionario().getUuid(), parserItem(pedidoDTO.getItemDTOList(), pedido), totalValue(pedidoDTO.getItemDTOList()))) {
            pedido = this.iPedidoRepository.save(pedido);

            for (ItemDTO itemDTO : pedidoDTO.getItemDTOList()) {
                Item item = new Item();
                LOGGER.info("Salvando itens");
                itemDTO.setPedido(pedido.getId());
                itemService.save(itemDTO);
                item.setQuantidade(itemDTO.getQuantidade());
                item.setProduto(produtoService.findByProdutoId(itemDTO.getProduto()));
                itemList.add(item);

            }

            LOGGER.info("Enviando e-mail");
            email.enviarEmailDataRetirada(pedido);
            LOGGER.info("E-mail enviado com sucesso");

        }
        return PedidoDTO.of(pedido);
    }

    public PedidoDTO findByid(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (pedidoOptional.isPresent()) {
            return PedidoDTO.of(pedidoOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Pedido findByPedidoId(Long id) {
        Optional<Pedido> pedidoOptional = this.iPedidoRepository.findById(id);

        if (pedidoOptional.isPresent()) {
            return pedidoOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validateUpdate(pedidoDTO, id);

        if (pedidoExistenteOptional.isPresent()) {
            Pedido pedidoExistente = pedidoExistenteOptional.get();

            LOGGER.info("Atualizando pedido... id:[{}]", pedidoExistente.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido existente:{}", pedidoExistente);

            pedidoExistente.setData(LocalDate.now());
            pedidoExistente.setFuncionario(funcionarioService.findByFuncionarioId(pedidoDTO.getFuncionario()));
            pedidoExistente.setPeriodoVenda(periodoVendaService.findByPeriodoVendaId(pedidoDTO.getPeriodoVenda()));

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
        pedidoDTO.setStatus("ATIVO");
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
        if (StringUtils.isEmpty(String.valueOf(pedidoDTO.getPeriodoVenda()))) {
            throw new IllegalArgumentException("Periodo de vendas não deve ser nulo");
        }
        if (iPedidoRepository.existsByCodPedido(pedidoDTO.getCodPedido())) {
            throw new IllegalArgumentException("O codigo do pedido já existe");
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
        for (int i = 1; i < 61; i++) {
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

    public double totalValue(List<ItemDTO> itemDTOS){
        double valorTotal = 0;
        for (ItemDTO itemDTO : itemDTOS){
            Produto produto = new Produto();
            produto.getPreco();
            valorTotal += (produto.getPreco() * itemDTO.getQuantidade());
        }
        return valorTotal;
    }
    private List<Item> parserItem(List<ItemDTO> itemDTOS, Pedido pedido){
        List<Item> items = new ArrayList<>();
        for (ItemDTO itemDTO : itemDTOS){
            Item item = new Item();
            item.setPedido(pedido);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setProduto(produtoService.findByProdutoId(itemDTO.getProduto()));
            items.add(item);
        }
        return items;
    }

    private boolean invoiceValidarPedido(String cnpjFornecedor, String uuid, List<Item> items, double totalValue) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<InvoiceDTO> httpEntity = new HttpEntity<>(InvoiceDTO.parser(cnpjFornecedor, uuid, items, totalValue));
        ResponseEntity<InvoiceDTO> responseEntity = restTemplate.exchange("http://10.2.54.25:9999/v2/api-docs", HttpMethod.POST, httpEntity, InvoiceDTO.class);
        if (responseEntity.getStatusCodeValue() == 200 || responseEntity.getStatusCodeValue() == 201) {
            return true;
        }
        throw new IllegalArgumentException("Não foi possivel fazer a validação na Api" + responseEntity.getStatusCodeValue());
    }


}







