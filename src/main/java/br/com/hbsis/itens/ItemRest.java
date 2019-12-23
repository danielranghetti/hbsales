package br.com.hbsis.itens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itens")
public class ItemRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemRest.class);
    private final ItemService itemService;

    @Autowired

    public ItemRest(ItemService itemService) {
        this.itemService = itemService;
    }
    @PostMapping
    public ItemDTO save(@RequestBody ItemDTO itemDTO){
        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payload: {}",itemDTO);

        return this.itemService.save(itemDTO);
    }
    @GetMapping("/id")
    public  ItemDTO find(@PathVariable("id") Long id){
        LOGGER.info("Recebendo find by ID...id: [{}]",id);
        return this.itemService.findById(id);
    }
    @DeleteMapping("/id")
    public void delete(@PathVariable("id") Long id){
        LOGGER.info("Recebendo delete para o item de ID: {}",id);
        this.itemService.delete(id);
    }
}
