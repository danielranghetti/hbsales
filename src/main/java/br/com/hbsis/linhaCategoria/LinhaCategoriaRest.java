package br.com.hbsis.linhaCategoria;


import br.com.hbsis.csv.CsvLinhaCategoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/linhaCategorias")
public class LinhaCategoriaRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhaCategoriaRest.class);
    private final LinhaCategoriaService linhaCategoriaService;
    private final CsvLinhaCategoria csvLinhaCategoria;

    @Autowired
    public LinhaCategoriaRest(LinhaCategoriaService linhaCategoriaService, CsvLinhaCategoria csvLinhaCategoria) {
        this.linhaCategoriaService = linhaCategoriaService;
        this.csvLinhaCategoria = csvLinhaCategoria;
    }

    @PostMapping
    public LinhaCategoriaDTO save(@RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payaload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.save(linhaCategoriaDTO);
    }

    @GetMapping("/{id}")
    public LinhaCategoriaDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.linhaCategoriaService.findByid(id);
    }

    @PutMapping("/{id}")
    public LinhaCategoriaDTO udpate(@PathVariable("id") Long id, @RequestBody LinhaCategoriaDTO linhaCategoriaDTO) {
        LOGGER.info("Recebendo Update para categoria de ID: {}", id);
        LOGGER.debug("Payload: {}", linhaCategoriaDTO);

        return this.linhaCategoriaService.update(linhaCategoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Usuário de ID: {}", id);

        this.linhaCategoriaService.delete(id);
    }

    @GetMapping("/exporta-csv-linhaCategorias")
    public void exportCsv(HttpServletResponse response) throws Exception {
        csvLinhaCategoria.csvToLinhaCategoriaExport(response);
    }

    @PostMapping("/importa-csv-linhaCategorias")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        csvLinhaCategoria.importFromCsvlinhaCategoria(file);
    }


}
