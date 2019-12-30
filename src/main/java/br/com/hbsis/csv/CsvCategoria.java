package br.com.hbsis.csv;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.categoria.ConexaoCategoria;
import br.com.hbsis.ferramentas.MascaraCnpj;
import br.com.hbsis.fornecedor.ConexaoFornecedor;
import br.com.hbsis.fornecedor.Fornecedor;
import com.opencsv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvCategoria {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvCategoria.class);


    private final MascaraCnpj mascaraCnpj;
    private final CategoriaService categoriaService;
    private final ConexaoFornecedor conexaoFornecedor;
    private final ConexaoCategoria conexaoCategoria;


    @Autowired
    public CsvCategoria(MascaraCnpj mascaraCnpj, CategoriaService categoriaService, ConexaoFornecedor conexaoFornecedor, ConexaoCategoria conexaoCategoria) {
        this.mascaraCnpj = mascaraCnpj;
        this.categoriaService = categoriaService;
        this.conexaoFornecedor = conexaoFornecedor;
        this.conexaoCategoria = conexaoCategoria;
    }

    public void csvTocategoriaExport(HttpServletResponse response) throws Exception {

        String nomeArquivo = "categorias.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nomeArquivo + "\"");
        PrintWriter Writer = response.getWriter();

        ICSVWriter csvWriter = new CSVWriterBuilder(Writer)
                .withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .build();

        String[] headerCSV = {"codigo_categoria", "nome_categoria", "razao_social_fornecedor", "cnpj_fornecedor "};
        csvWriter.writeNext(headerCSV);

        for (Categoria linha : conexaoCategoria.findAll()) {
            csvWriter.writeNext(new String[]{linha.getCodigoCategoria(),linha.getNomeCategoria(),linha.getFornecedor().getRazaoSocial(),mascaraCnpj.formatCnpj(linha.getFornecedor().getCnpj())}
            );
        }

    }

    private List<Categoria> csvToCategoria(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withSkipLines(1).build();
        List<Categoria> leitura = new ArrayList<>();
        List<String[]> linhas = csvReader.readAll();

        for (String[] lista : linhas) {
            try {
                String[] resultado = lista[0].replaceAll("\"", "").split(";");

                Categoria categoria = new Categoria();

                String codigoCategoria = resultado[0];
                String cnpj = resultado[3].replaceAll("[^0-9]", "");
                String nomeCategoria = resultado[1];

                if (conexaoCategoria.existsByCodigoCategoria(codigoCategoria)) {
                    LOGGER.info("Categoria: {}", codigoCategoria + " já existe");
                } else {
                    if (conexaoFornecedor.existsByCnpj(cnpj)) {
                        if (!conexaoCategoria.existsByCodigoCategoria(codigoCategoria)) {
                            Fornecedor fornecedor;
                            categoria.setCodigoCategoria(codigoCategoria);
                            categoria.setNomeCategoria(nomeCategoria);
                            fornecedor = conexaoFornecedor.findByFornecedorCnpj(cnpj);
                            LOGGER.info("Categoria: {}", codigoCategoria + " salvando");

                            categoria.setFornecedor(fornecedor);
                            leitura.add(categoria);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conexaoCategoria.saveAll(leitura);
    }

    public void importFromCsv(MultipartFile file) throws Exception {
        List<Categoria> categorias = this.csvToCategoria(file);
        categoriaService.saveAll(categorias);
    }

}
