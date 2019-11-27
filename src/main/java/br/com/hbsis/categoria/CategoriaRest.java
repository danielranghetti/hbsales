package br.com.hbsis.categoria;


import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@RestController
@RequestMapping("/categorias")
public class CategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaRest.class);
    private final CategoriaService categoriaService;


    @Autowired
    public CategoriaRest(CategoriaService categoriaService){this.categoriaService = categoriaService;}

    @PostMapping
    public CategoriaDTO save(@RequestBody CategoriaDTO categoriaDTO) {
        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payaload: {}", categoriaDTO);

        return this.categoriaService.save(categoriaDTO);
    }

    @GetMapping("/exporta-csv-categorias")
    public void exportCSV(HttpServletResponse response) throws Exception {

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

        String headerCSV[] = {"id da categoria", "codigo da categoria", "nome da categoria", "id do fornecedor"};
        csvWriter.writeNext(headerCSV);

        for (Categoria linha : categoriaService.findAll()) {
            csvWriter.writeNext(new String[] {linha.getId().toString(), linha.getCodigoCategoria().toString(),  linha.getNomeCategoria(), linha.getFornecedor().getId().toString()}
                    );
        }

    }



    @GetMapping("/{id}")
    public CategoriaDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.categoriaService.findById(id);
    }
    @PutMapping("/{id}")
    public CategoriaDTO udpate(@PathVariable("id") Long id, @RequestBody CategoriaDTO categoriaDTO){
        LOGGER.info("Recebendo Update para categoria de ID: {}", id);
        LOGGER.debug("Payload: {}", categoriaDTO);

        return this.categoriaService.update(categoriaDTO, id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Usuário de ID: {}", id);

        this.categoriaService.delete(id);
    }
}
