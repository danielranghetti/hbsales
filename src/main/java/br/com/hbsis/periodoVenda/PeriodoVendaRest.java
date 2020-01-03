package br.com.hbsis.periodoVenda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/periodovendas")
public class PeriodoVendaRest {

    public static final Logger LOGGER = LoggerFactory.getLogger(PeriodoVendaRest.class);

    public final PeriodoVendaService periodoVendaService;

    @Autowired
    public PeriodoVendaRest(PeriodoVendaService periodoVendaService) {
        this.periodoVendaService = periodoVendaService;
    }

    @PostMapping
    public PeriodoVendaDTO save(@RequestBody PeriodoVendaDTO periodoVendaDTO) {
        LOGGER.info("Recebendo solicitação de persistência de periodo de venda...");
        LOGGER.debug("Payaload: {}", periodoVendaDTO);

        return this.periodoVendaService.save(periodoVendaDTO);
    }

    @GetMapping("/{id}")
    public PeriodoVendaDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.periodoVendaService.findById(id);
    }

    @PutMapping("/{id}")
    public PeriodoVendaDTO udpate(@PathVariable("id") Long id, @RequestBody PeriodoVendaDTO periodoVendaDTO) {
        LOGGER.info("Recebendo Update para Usuário de ID: {}", id);
        LOGGER.debug("Payload: {}", periodoVendaDTO);

        return this.periodoVendaService.update(periodoVendaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para periodo de venda de ID: {}", id);

        this.periodoVendaService.delete(id);
    }
}

