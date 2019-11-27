package br.com.hbsis.categoria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
