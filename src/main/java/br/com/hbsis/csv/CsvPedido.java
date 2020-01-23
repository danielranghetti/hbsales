package br.com.hbsis.csv;

import br.com.hbsis.ferramentas.MascaraCnpj;
import br.com.hbsis.funcionario.ConexaoFuncionario;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.itens.ConexaoItem;
import br.com.hbsis.itens.Item;
import br.com.hbsis.pedido.ConexaoPedido;
import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.periodoVenda.ConexaoPeriodoVenda;
import br.com.hbsis.periodoVenda.PeriodoVenda;
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
    private final ConexaoPedido conexaoPedido;
    private final ConexaoPeriodoVenda conexaoPeriodoVenda;
    private final ConexaoFuncionario conexaoFuncionario;
    private final MascaraCnpj mascaraCnpj;
    private final ConexaoItem conexaoItem;

    @Autowired
    public CsvPedido(ConexaoPedido conexaoPedido, ConexaoPeriodoVenda conexaoPeriodoVenda,
                     ConexaoFuncionario conexaoFuncionario, MascaraCnpj mascaraCnpj, ConexaoItem conexaoItem) {
        this.conexaoPedido = conexaoPedido;
        this.conexaoPeriodoVenda = conexaoPeriodoVenda;
        this.conexaoFuncionario = conexaoFuncionario;
        this.mascaraCnpj = mascaraCnpj;
        this.conexaoItem = conexaoItem;

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
        periodoVendas = conexaoPeriodoVenda.findByPeriodoVendaId(id);
        List<Item> itemList;
        List<Pedido> pedidos;

        pedidos = conexaoPedido.findByPeriodoVenda(periodoVendas);

        for (Pedido pedido : pedidos) {
            itemList = conexaoItem.findByPedido(pedido);
            for (Item item: itemList)
            icsvWriter.writeNext(new String[]{item.getProduto().getNome(), String.valueOf(item.getQuantidade()), pedido.getPeriodoVenda().getFornecedor().getRazaoSocial() + "---" + mascaraCnpj.formatCnpj(pedido.getPeriodoVenda().getFornecedor().getCnpj())});
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
        funcionario = conexaoFuncionario.findByFuncionarioId(id);
        List<Item> items;
        List<Pedido> pedidos;

        pedidos = conexaoPedido.findByFuncionario(funcionario);
        for (Pedido pedido : pedidos) {
            items = conexaoItem.findByPedido(pedido);
            for (Item item : items) {
                icsvWriter.writeNext(new String[]{pedido.getFuncionario().getNome(), item.getProduto().getNome(), String.valueOf(item.getQuantidade()), pedido.getPeriodoVenda().getFornecedor().getRazaoSocial() + "--" + mascaraCnpj.formatCnpj(pedido.getPeriodoVenda().getFornecedor().getCnpj())});
            }
        }
    }



}
