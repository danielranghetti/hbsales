package br.com.hbsis.pedido;



import br.com.hbsis.csv.CsvPedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoRest.class);
    private final PedidoService pedidoService;
    private final CsvPedido csvPedido;

    @Autowired

    public PedidoRest(PedidoService pedidoService, CsvPedido csvPedido) {
        this.pedidoService = pedidoService;
        this.csvPedido = csvPedido;
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
    public PedidoDTO udpate(@PathVariable("id") Long id, @RequestBody PedidoDTO pedidoDTO) {
        LOGGER.info("Recebendo Update para Pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);

        return this.pedidoService.update(pedidoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Usuário de ID: {}", id);

        this.pedidoService.delete(id);
    }
    @GetMapping("/exportaPedidosPeriodVenda/{id}")
    public void findAll(@PathVariable("id") Long id,HttpServletResponse response) throws Exception{
        csvPedido.csvPedidoPeriodoVendasExport(response, id);
    }

    @GetMapping("/exportaPedidosFuncionarios/{id}")
    public void findAllFuncionario(@PathVariable("id") Long id,HttpServletResponse response) throws Exception{
        csvPedido.csvPedidoFuncionarioExport(id, response);
    }
    @GetMapping("/pedidoAtivoRetirado/{id}")
    public List<PedidoDTO> findAll1(@PathVariable Long id) {
        return this.pedidoService.findAllByFornecedorId(id);
    }

    @PutMapping("/cancelaPedido/{id}")
    public PedidoDTO updateCan(@PathVariable("id") Long id, PedidoDTO pedidoDTO){
        LOGGER.info("Recebendo Update para Pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);
        return this.pedidoService.updateCancelar(pedidoDTO,id);
    }
    @PutMapping("/retirarPedido/{id}")
    public PedidoDTO updateRet(@PathVariable("id") Long id, PedidoDTO pedidoDTO){
        LOGGER.info("Recebendo Update para Pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);
        return this.pedidoService.updateRetira(pedidoDTO,id);
    }

}
