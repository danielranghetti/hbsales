package br.com.hbsis.csv;

import br.com.hbsis.ferramentas.MascaraCnpj;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.pedido.IPedidoRepository;
import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.periodoVenda.PeriodoVenda;
import br.com.hbsis.periodoVenda.PeriodoVendaService;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CsvPedido {
    private final IPedidoRepository iPedidoRepository;
    private final PeriodoVendaService periodoVendaService;
    private final FuncionarioService funcionarioService;
    private final MascaraCnpj mascaraCnpj;

    @Autowired
    public CsvPedido(IPedidoRepository iPedidoRepository, PeriodoVendaService periodoVendaService, FuncionarioService funcionarioService, MascaraCnpj mascaraCnpj) {
        this.iPedidoRepository = iPedidoRepository;
        this.periodoVendaService = periodoVendaService;
        this.funcionarioService = funcionarioService;
        this.mascaraCnpj = mascaraCnpj;
    }

    public void csvPedidoPeriodoVendasExport(HttpServletResponse response, Long id) throws Exception {
        String nomeArquivo = "pedidos-fornecedor.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(Writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] escreveCsv = {"Nome do Produto", "Quantidade", "fornecedor"};
        icsvWriter.writeNext(escreveCsv);
        PeriodoVenda periodoVendas;
        periodoVendas = periodoVendaService.findByPeriodoVendaId(id);

        List<Pedido> pedidos;

        pedidos = iPedidoRepository.findByPeriodoVenda(periodoVendas);

        for (Pedido pedido : pedidos) {
            icsvWriter.writeNext(new String[]{pedido.getProduto().getNome(), String.valueOf(pedido.getQtdCompra()), pedido.getPeriodoVenda().getFornecedor().getRazaoSocial() + "---" + mascaraCnpj.formatCnpj(pedido.getPeriodoVenda().getFornecedor().getCnpj())});
        }
    }
    public void csvPedidoFuncionarioExport(Long id, HttpServletResponse response) throws Exception {
        String nomeArquivo = "pedido-funcionario.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(Writer).withSeparator(';').withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();
        String[] ecreveCsv = {"Nome do Funcionário", "Nome do Produto", "Quantidade", "fornecedor"};
        icsvWriter.writeNext(ecreveCsv);

        Funcionario funcionario;
        funcionario = funcionarioService.findByFuncionarioId(id);
        List<Pedido> pedidos;

        pedidos = iPedidoRepository.findByFuncionario(funcionario);
        for (Pedido pedido : pedidos) {
            icsvWriter.writeNext(new String[]{pedido.getFuncionario().getNome(), pedido.getProduto().getNome(), String.valueOf(pedido.getQtdCompra()), pedido.getPeriodoVenda().getFornecedor().getRazaoSocial() + "--" + mascaraCnpj.formatCnpj(pedido.getPeriodoVenda().getFornecedor().getCnpj())});
        }
    }



}
