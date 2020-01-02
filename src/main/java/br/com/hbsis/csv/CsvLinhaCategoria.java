package br.com.hbsis.csv;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.ConexaoCategoria;
import br.com.hbsis.linhaCategoria.ConexaoLinhaCategoria;
import br.com.hbsis.linhaCategoria.LinhaCategoria;
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
public class CsvLinhaCategoria {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvLinhaCategoria.class);


    private final ConexaoLinhaCategoria conexaoLinhaCategoria;
    private final ConexaoCategoria conexaoCategoria;

    @Autowired
    public CsvLinhaCategoria(ConexaoLinhaCategoria conexaoLinhaCategoria, ConexaoCategoria conexaoCategoria) {
        this.conexaoLinhaCategoria = conexaoLinhaCategoria;
        this.conexaoCategoria = conexaoCategoria;

    }


    public void csvToLinhaCategoriaExport(HttpServletResponse response) throws Exception {
        String nomeArquivo = "linhaCategorias.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"");
        PrintWriter writer = response.getWriter();

        ICSVWriter icsvWriter = new CSVWriterBuilder(writer).withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER).withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String[] headerCSV = {"codigo", "nome", "codigo_categoria", "nome_categoria"};
        icsvWriter.writeNext(headerCSV);

        for (LinhaCategoria linha : conexaoLinhaCategoria.findAll()) {
            icsvWriter.writeNext(new String[]{linha.getCodLinhaCategoria(), linha.getNomeLinha(), linha.getCategoria().getCodigoCategoria(), linha.getCategoria().getNomeCategoria()}
            );
        }


    }

    private List<LinhaCategoria> csvToLinhaCategoria(MultipartFile multipartFile) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(multipartFile.getInputStream());

        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withSkipLines(1).build();


        List<LinhaCategoria> leitura = new ArrayList<>();
        List<String[]> linhas = csvReader.readAll();

        for (String[] lista : linhas) {
            try {

                String[] resultado = lista[0].replaceAll("\"", "").split(";");

                LinhaCategoria linhaCategoria = new LinhaCategoria();
                Categoria categoria;

                String codLinhacat = resultado[0];
                String nomeLinha = resultado[1];
                String codCategoria = resultado[2];

                if (conexaoCategoria.existsByCodigoCategoria(codCategoria)) {

                    if (conexaoLinhaCategoria.existsByCodLinhaCategoria(codLinhacat)) {
                        LOGGER.info("Linha categoria: {}", codLinhacat + " já existe");

                    } else if (!conexaoLinhaCategoria.existsByCodLinhaCategoria(codLinhacat)) {
                        LOGGER.info("Linha categoria: {}", codLinhacat + " salvando");
                        linhaCategoria.setCodLinhaCategoria(codLinhacat);
                        linhaCategoria.setNomeLinha(nomeLinha);
                        categoria = conexaoCategoria.findByCodigoCategoria1(codCategoria);
                        linhaCategoria.setCategoria(categoria);
                        leitura.add(linhaCategoria);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return conexaoLinhaCategoria.saveAll(leitura);
    }

    public void importFromCsvlinhaCategoria(MultipartFile file) throws Exception {
        List<LinhaCategoria> linhaCategorias = this.csvToLinhaCategoria(file);
        conexaoLinhaCategoria.saveAll(linhaCategorias);
    }
}
