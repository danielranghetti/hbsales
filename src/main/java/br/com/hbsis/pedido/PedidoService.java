package br.com.hbsis.pedido;

import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.periodoVenda.PeriodoVenda;
import br.com.hbsis.periodoVenda.PeriodoVendaService;
import br.com.hbsis.produto.Produto;
import br.com.hbsis.produto.ProdutoService;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
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

    //TODO: 13/12/2019 se não está sendo utilizado, corta fora
    private final PeriodoVendaService periodoVendaService;
    private final ProdutoService produtoService;


    public PedidoService(IPedidoRepository iPedidoRepository, FuncionarioService funcionarioService, PeriodoVendaService periodoVendaService, ProdutoService produtoService) {
        this.iPedidoRepository = iPedidoRepository;
        this.funcionarioService = funcionarioService;
        this.periodoVendaService = periodoVendaService;
        this.produtoService = produtoService;
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

    public void csvPedidoPeriodoVendasExport(HttpServletResponse response, Long id) throws Exception {
        String nomeArquivo = "pedidos-fornecedor.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(Writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String escreveCsv[] = {"Nome do Produto", "Quantidade", "fornecedor"};
        icsvWriter.writeNext(escreveCsv);
        PeriodoVenda periodoVendas;
        periodoVendas = periodoVendaService.findByPeriodoVendaId(id);

        List<Pedido> pedidos;

        pedidos = iPedidoRepository.findByPeriodoVenda(periodoVendas);

        for (Pedido pedido : pedidos) {
            icsvWriter.writeNext(new String[]{pedido.getProduto().getNome(), String.valueOf(pedido.getQtdCompra()), pedido.getPeriodoVenda().getFornecedor().getRazaoSocial() + "---" + mascaraCnpj(pedido.getPeriodoVenda().getFornecedor().getCnpj())});
        }
    }

    public String mascaraCnpj(String CNPJ) {
        String mascara = CNPJ.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        return mascara;
    }

    public void csvPedidoFuncionario(Long id, HttpServletResponse response) throws Exception {
        String nomeArquivo = "pedido-funcionario.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(Writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();
        String ecreveCsv[] = {"Nome do Funcionário", "Nome do Produto", "Quantidade", "fornecedor"};
        icsvWriter.writeNext(ecreveCsv);

        Funcionario funcionario;
        funcionario = funcionarioService.findByFuncionarioId(id);
        List<Pedido> pedidos;

        pedidos = iPedidoRepository.findByFuncionario(funcionario);
        for (Pedido pedido : pedidos) {
            icsvWriter.writeNext(new String[]{pedido.getFuncionario().getNomeFun(), pedido.getProduto().getNome(), String.valueOf(pedido.getQtdCompra()), pedido.getPeriodoVenda().getFornecedor().getRazaoSocial() + "--" + mascaraCnpj(pedido.getPeriodoVenda().getFornecedor().getCnpj())});
        }
    }


    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> pedidoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validate(pedidoDTO);

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
            if (pedidoExistente.getStatus().equals("RETIRADO")) {
                throw new IllegalArgumentException("Já Retirado não podendo ser mas Cancelado");
            }
            if (pedidoExistente.getStatus().equals("CANCELADO")) {
                throw new IllegalArgumentException("Já cancelado");
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

}







