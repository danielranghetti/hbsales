package br.com.hbsis.csv;

import br.com.hbsis.categoria.Categoria;
import br.com.hbsis.categoria.CategoriaService;
import br.com.hbsis.categoria.ICategoriaRepository;
import br.com.hbsis.linhaCategoria.ILinhaCategoriaRepository;
import br.com.hbsis.linhaCategoria.LinhaCategoria;
import br.com.hbsis.linhaCategoria.LinhaCategoriaService;
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

     private final CategoriaService categoriaService;
     private final LinhaCategoriaService linhaCategoriaService;
     private final ILinhaCategoriaRepository iLinhaCategoriaRepository;
     private final ICategoriaRepository iCategoriaRepository;

     @Autowired
    public CsvLinhaCategoria(CategoriaService categoriaService, LinhaCategoriaService linhaCategoriaService, ILinhaCategoriaRepository iLinhaCategoriaRepository, ICategoriaRepository iCategoriaRepository) {
        this.categoriaService = categoriaService;
        this.linhaCategoriaService = linhaCategoriaService;
        this.iLinhaCategoriaRepository = iLinhaCategoriaRepository;
        this.iCategoriaRepository = iCategoriaRepository;
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

        for (LinhaCategoria linha : iLinhaCategoriaRepository.findAll()) {
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
                Categoria categoria = new Categoria();

                String codLinhacat = resultado[0];
                String nomeLinha = resultado[1];
                String codCategoria = resultado[2];

                if (iLinhaCategoriaRepository.existsByCodLinhaCategoria(codLinhacat)) {
                    LOGGER.info("Linha categoria: {}", codLinhacat + " já existe");
                }

                else if (iCategoriaRepository.existsByCodigoCategoria(codCategoria)) {

                    linhaCategoria.setCodLinhaCategoria(codLinhacat);
                    linhaCategoria.setNomeLinha(nomeLinha);
                    categoria = categoriaService.findByCodigoCategoria(codCategoria);

                    linhaCategoria.setCategoria(categoria);
                    leitura.add(linhaCategoria);
                }

                else if (!iLinhaCategoriaRepository.existsByCodLinhaCategoria(codLinhacat)){
                    LOGGER.info("Linha categoria: {}", codLinhacat + " salvando");
                    linhaCategoria.setCategoria(categoria);
                    leitura.add(linhaCategoria);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return iLinhaCategoriaRepository.saveAll(leitura);
    }
    public void importFromCsvlinhaCategoria(MultipartFile file) throws Exception {
        List<LinhaCategoria> linhaCategorias = this.csvToLinhaCategoria(file);
        linhaCategoriaService.saveAll(linhaCategorias);
    }
}
