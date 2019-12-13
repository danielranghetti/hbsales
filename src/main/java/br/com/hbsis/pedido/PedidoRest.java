package br.com.hbsis.pedido;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoRest.class);
    private final PedidoService pedidoService;

    @Autowired

    public PedidoRest(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    @PostMapping
    public  PedidoDTO save(@RequestBody PedidoDTO pedidoDTO){
        LOGGER.info("Recebendo persistência de pedido");
        LOGGER.debug("Payaload: {}", pedidoDTO);

        return this.pedidoService.save(pedidoDTO);
    }
    @GetMapping("/{id}")
    public PedidoDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.pedidoService.findByid(id);
    }
    @PutMapping("/{id}")
    public PedidoDTO udpate(@PathVariable("id") Long id, @RequestBody PedidoDTO usuarioDTO) {
        LOGGER.info("Recebendo Update para Usuário de ID: {}", id);
        LOGGER.debug("Payload: {}", usuarioDTO);

        return this.pedidoService.update(usuarioDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Usuário de ID: {}", id);

        this.pedidoService.delete(id);
    }


}