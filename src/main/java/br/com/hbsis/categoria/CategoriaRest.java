package br.com.hbsis.categoria;


import br.com.hbsis.csv.CsvCategoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/categorias")
public class CategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaRest.class);
    private final CategoriaService categoriaService;
    private final CsvCategoria csvCategoria;


    @Autowired
    public CategoriaRest(CategoriaService categoriaService, CsvCategoria csvCategoria) {
        this.categoriaService = categoriaService;
        this.csvCategoria = csvCategoria;
    }

    @PostMapping("/importa-csv-categorias")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        csvCategoria.importFromCsv(file);
    }

    @PostMapping
    public CategoriaDTO save(@RequestBody CategoriaDTO categoriaDTO) {
        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payaload: {}", categoriaDTO);

        return this.categoriaService.save(categoriaDTO);
    }

    @GetMapping("/exporta-csv-categorias")
    public void findAll(HttpServletResponse response) throws Exception {
        csvCategoria.csvTocategoriaExport(response);
    }


    @GetMapping("/{id}")
    public CategoriaDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.categoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoriaDTO udpate(@PathVariable("id") Long id, @RequestBody CategoriaDTO categoriaDTO) {
        LOGGER.info("Recebendo Update para categoria de ID: {}", id);
        LOGGER.debug("Payload: {}", categoriaDTO);

        return this.categoriaService.update(categoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Categoria de ID: {}", id);

        this.categoriaService.delete(id);
    }
}
